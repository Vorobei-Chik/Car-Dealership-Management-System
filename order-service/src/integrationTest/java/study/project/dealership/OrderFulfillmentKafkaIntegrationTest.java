package study.project.dealership;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import study.project.dealership.common.messaging.KafkaTopics;
import study.project.dealership.common.messaging.OrderApprovedEvent;
import study.project.dealership.common.messaging.OrderRejectedEvent;
import study.project.dealership.common.messaging.OrderType;
import study.project.dealership.domain.order.CustomOrder;
import study.project.dealership.domain.order.OrderConfiguration;
import study.project.dealership.domain.order.StockOrder;
import study.project.dealership.domain.valueobject.carinfo.Color;
import study.project.dealership.domain.valueobject.orderinfo.CustomOrderStatus;
import study.project.dealership.domain.valueobject.orderinfo.StockOrderStatus;
import study.project.dealership.infrastructure.database.repository.OrderJpaRepository;
import study.project.dealership.infrastructure.messaging.OrderFulfillmentListener;
import study.project.dealership.infrastructure.messaging.inbox.ProcessedEventRepository;
import study.project.dealership.support.OrderIntegrationContainers;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Testcontainers
@SpringBootTest
class OrderFulfillmentKafkaIntegrationTest extends OrderIntegrationContainers {

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registerDataSource(registry);
        registerKafka(registry);
    }

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OrderJpaRepository orderJpaRepository;

    @Autowired
    ProcessedEventRepository processedEventRepository;

    @Test
    void orderApprovedEvent_updatesStockOrderToReadyForPickup() throws Exception {
        UUID traceId = UUID.randomUUID();
        StockOrder order = StockOrder.create(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );
        order.setStatus(StockOrderStatus.PAID);
        orderJpaRepository.save(order);

        kafkaTemplate.send(KafkaTopics.ORDER_APPROVED, order.getId().toString(),
                objectMapper.writeValueAsString(
                        new OrderApprovedEvent(order.getId(), OrderType.STOCK, traceId)
                )).get();

        await().atMost(Duration.ofSeconds(20)).untilAsserted(() -> {
            StockOrder updated = (StockOrder) orderJpaRepository.findById(order.getId()).orElseThrow();
            assertThat(updated.getStatus()).isEqualTo(StockOrderStatus.READY_FOR_PICKUP);
            assertThat(processedEventRepository.existsById(traceId)).isTrue();
        });
    }

    @Test
    void orderApprovedEvent_updatesCustomOrderToAwaitingDelivery() throws Exception {
        UUID traceId = UUID.randomUUID();
        OrderConfiguration config = new OrderConfiguration();
        config.setModelId(UUID.randomUUID());
        config.setEngineId(UUID.randomUUID());
        config.setGearBoxId(UUID.randomUUID());
        config.setTransmissionId(UUID.randomUUID());
        config.setWheelId(UUID.randomUUID());
        config.setInteriorId(UUID.randomUUID());
        config.setRudderId(UUID.randomUUID());
        config.setColor(new Color("#AABBCC"));
        config.setStandard(false);

        CustomOrder order = CustomOrder.create(UUID.randomUUID(), UUID.randomUUID(), config);
        order.setStatus(CustomOrderStatus.PAID);
        orderJpaRepository.save(order);

        kafkaTemplate.send(KafkaTopics.ORDER_APPROVED, order.getId().toString(),
                objectMapper.writeValueAsString(
                        new OrderApprovedEvent(order.getId(), OrderType.CUSTOM, traceId)
                )).get();

        await().atMost(Duration.ofSeconds(20)).untilAsserted(() -> {
            CustomOrder updated = (CustomOrder) orderJpaRepository.findById(order.getId()).orElseThrow();
            assertThat(updated.getStatus()).isEqualTo(CustomOrderStatus.AWAITING_DELIVERY);
            assertThat(processedEventRepository.existsById(traceId)).isTrue();
        });
    }

    @Test
    void orderApprovedEvent_logsApprovalWhileHandling() throws Exception {
        UUID traceId = UUID.randomUUID();
        StockOrder order = StockOrder.create(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );
        order.setStatus(StockOrderStatus.PAID);
        orderJpaRepository.save(order);

        Logger listenerLogger = (Logger) LoggerFactory.getLogger(OrderFulfillmentListener.class);
        ListAppender<ILoggingEvent> logAppender = new ListAppender<>();
        logAppender.start();
        listenerLogger.addAppender(logAppender);

        try {
            kafkaTemplate.send(KafkaTopics.ORDER_APPROVED, order.getId().toString(),
                    objectMapper.writeValueAsString(
                            new OrderApprovedEvent(order.getId(), OrderType.STOCK, traceId)
                    )).get();

            await().atMost(Duration.ofSeconds(20)).untilAsserted(() -> assertThat(logAppender.list).anyMatch(event ->
                    event.getFormattedMessage().contains("approved by storage")));
        } finally {
            listenerLogger.detachAppender(logAppender);
        }
    }

    @Test
    void orderRejectedEvent_cancelsStockOrder() throws Exception {
        UUID traceId = UUID.randomUUID();
        StockOrder order = StockOrder.create(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );
        order.setStatus(StockOrderStatus.PAID);
        orderJpaRepository.save(order);

        kafkaTemplate.send(KafkaTopics.ORDER_REJECTED, order.getId().toString(),
                objectMapper.writeValueAsString(
                        new OrderRejectedEvent(order.getId(), OrderType.STOCK, traceId, "Insufficient stock")
                )).get();

        await().atMost(Duration.ofSeconds(20)).untilAsserted(() -> {
            StockOrder updated = (StockOrder) orderJpaRepository.findById(order.getId()).orElseThrow();
            assertThat(updated.getStatus()).isEqualTo(StockOrderStatus.CANCELLED);
            assertThat(processedEventRepository.existsById(traceId)).isTrue();
        });
    }
}
