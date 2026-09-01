package study.project.dealership;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import study.project.dealership.common.messaging.KafkaTopics;
import study.project.dealership.contracts.order.OrderService;
import study.project.dealership.contracts.order.request.RequestPayStockOrder;
import study.project.dealership.domain.order.StockOrder;
import study.project.dealership.domain.valueobject.orderinfo.StockOrderStatus;
import study.project.dealership.infrastructure.database.repository.OrderJpaRepository;
import study.project.dealership.infrastructure.messaging.outbox.OutboxJpaRepository;
import study.project.dealership.support.OrderIntegrationContainers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class OrderPayOutboxIntegrationTest extends OrderIntegrationContainers {

    private static final UUID MANAGER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID CLIENT_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registerDataSource(registry);
        registerKafka(registry);
    }

    @Autowired
    OrderService orderService;

    @Autowired
    OrderJpaRepository orderJpaRepository;

    @Autowired
    OutboxJpaRepository outboxJpaRepository;

    @BeforeEach
    void authenticateManager() {
        var auth = new UsernamePasswordAuthenticationToken(
                MANAGER_ID.toString(),
                "n/a",
                List.of(new SimpleGrantedAuthority("ROLE_MANAGER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void payStockOrder_persistsPaidStatusAndOutboxMessage() {
        UUID carId = UUID.randomUUID();
        StockOrder order = StockOrder.create(CLIENT_ID, MANAGER_ID, carId);
        order.setStatus(StockOrderStatus.AWAITING_PAYMENT);
        UUID orderId = orderJpaRepository.save(order).getId();

        StockOrder paid = orderService.payStockOrder(new RequestPayStockOrder(orderId));

        assertThat(paid.getStatus()).isEqualTo(StockOrderStatus.PAID);
        assertThat(outboxJpaRepository.findUnpublished()).anyMatch(message ->
                KafkaTopics.ORDER_SENT_FOR_APPROVAL.equals(message.getTopic())
                        && message.getMessageKey().equals(orderId.toString())
                        && message.getPayload().contains(orderId.toString())
                        && message.getPayload().contains(carId.toString()));
    }
}
