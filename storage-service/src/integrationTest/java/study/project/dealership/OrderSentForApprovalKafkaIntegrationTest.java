package study.project.dealership;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import study.project.dealership.common.messaging.KafkaTopics;
import study.project.dealership.common.messaging.OrderSentForApprovalEvent;
import study.project.dealership.common.messaging.OrderType;
import study.project.dealership.domain.assembly.AssemblyOrderStatus;
import study.project.dealership.infrastructure.database.repository.AssemblyOrderJpaRepository;
import study.project.dealership.infrastructure.messaging.inbox.ProcessedEventRepository;
import study.project.dealership.infrastructure.messaging.outbox.OutboxJpaRepository;
import study.project.dealership.infrastructure.messaging.outbox.OutboxPublisher;
import study.project.dealership.support.IntegrationContainers;
import study.project.dealership.support.StorageCatalogFixture;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Testcontainers
@SpringBootTest
class OrderSentForApprovalKafkaIntegrationTest extends IntegrationContainers {

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registerDataSource(registry);
        registerKafka(registry);
        registerGrpc(registry);
    }

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    StorageCatalogFixture catalogFixture;

    @Autowired
    AssemblyOrderJpaRepository assemblyOrderJpaRepository;

    @Autowired
    ProcessedEventRepository processedEventRepository;

    @Autowired
    OutboxJpaRepository outboxJpaRepository;

    @Autowired
    OutboxPublisher outboxPublisher;

    @Test
    void kafkaMessage_stockOrderApproved_createsAssemblyAndOutboxApprovedEvent() throws Exception {
        var seed = catalogFixture.seedCatalogWithStockCar(true);
        UUID orderId = UUID.randomUUID();
        UUID traceId = UUID.randomUUID();

        OrderSentForApprovalEvent event = new OrderSentForApprovalEvent(
                orderId, OrderType.STOCK, traceId, seed.carId(),
                null, null, null, null, null, null, null
        );
        kafkaTemplate.send(KafkaTopics.ORDER_SENT_FOR_APPROVAL, orderId.toString(),
                objectMapper.writeValueAsString(event)).get();

        await().atMost(Duration.ofSeconds(20)).untilAsserted(() -> {
            assertThat(assemblyOrderJpaRepository.findAll()).anyMatch(a ->
                    a.getSourceOrderId().equals(orderId)
                            && a.getStatus() == AssemblyOrderStatus.ASSEMBLED);
            assertThat(processedEventRepository.existsById(traceId)).isTrue();
        });

        outboxPublisher.publishPending();

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() ->
                assertThat(outboxJpaRepository.findUnpublished()).noneMatch(m ->
                        KafkaTopics.ORDER_APPROVED.equals(m.getTopic())
                                && m.getTraceId().equals(traceId)));
    }

    @Test
    void kafkaMessage_stockOrderRejected_createsFailedAssemblyAndOutboxRejectedEvent() throws Exception {
        var seed = catalogFixture.seedCatalogWithStockCar(false);
        UUID orderId = UUID.randomUUID();
        UUID traceId = UUID.randomUUID();

        OrderSentForApprovalEvent event = new OrderSentForApprovalEvent(
                orderId, OrderType.STOCK, traceId, seed.carId(),
                null, null, null, null, null, null, null
        );
        kafkaTemplate.send(KafkaTopics.ORDER_SENT_FOR_APPROVAL, orderId.toString(),
                objectMapper.writeValueAsString(event)).get();

        await().atMost(Duration.ofSeconds(20)).untilAsserted(() -> {
            assertThat(assemblyOrderJpaRepository.findAll()).anyMatch(a ->
                    a.getSourceOrderId().equals(orderId)
                            && a.getStatus() == AssemblyOrderStatus.FAIL);
            assertThat(processedEventRepository.existsById(traceId)).isTrue();
        });

        outboxPublisher.publishPending();

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() ->
                assertThat(outboxJpaRepository.findUnpublished()).noneMatch(m ->
                        KafkaTopics.ORDER_REJECTED.equals(m.getTopic())
                                && m.getTraceId().equals(traceId)));
    }

    @Test
    void kafkaMessage_duplicateTraceId_isProcessedOnlyOnce() throws Exception {
        var seed = catalogFixture.seedCatalogWithStockCar(true);
        UUID orderId = UUID.randomUUID();
        UUID traceId = UUID.randomUUID();

        OrderSentForApprovalEvent event = new OrderSentForApprovalEvent(
                orderId, OrderType.STOCK, traceId, seed.carId(),
                null, null, null, null, null, null, null
        );
        String payload = objectMapper.writeValueAsString(event);
        kafkaTemplate.send(KafkaTopics.ORDER_SENT_FOR_APPROVAL, orderId.toString(), payload).get();
        kafkaTemplate.send(KafkaTopics.ORDER_SENT_FOR_APPROVAL, orderId.toString(), payload).get();

        await().atMost(Duration.ofSeconds(20)).untilAsserted(() ->
                assertThat(processedEventRepository.existsById(traceId)).isTrue());

        long assemblyCount = assemblyOrderJpaRepository.findAll().stream()
                .filter(a -> a.getSourceOrderId().equals(orderId))
                .count();
        assertThat(assemblyCount).isEqualTo(1);
    }
}
