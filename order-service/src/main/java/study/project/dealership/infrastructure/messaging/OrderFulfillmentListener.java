package study.project.dealership.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.project.dealership.abstractions.repository.OrderRepository;
import study.project.dealership.common.messaging.KafkaTopics;
import study.project.dealership.common.messaging.OrderApprovedEvent;
import study.project.dealership.common.messaging.OrderRejectedEvent;
import study.project.dealership.common.messaging.OrderType;
import study.project.dealership.domain.order.CustomOrder;
import study.project.dealership.domain.order.Order;
import study.project.dealership.domain.order.StockOrder;
import study.project.dealership.domain.valueobject.orderinfo.CustomOrderStatus;
import study.project.dealership.domain.valueobject.orderinfo.StockOrderStatus;
import study.project.dealership.infrastructure.messaging.inbox.ProcessedEvent;
import study.project.dealership.infrastructure.messaging.inbox.ProcessedEventRepository;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class OrderFulfillmentListener {

    private static final Logger log = LoggerFactory.getLogger(OrderFulfillmentListener.class);

    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final ProcessedEventRepository processedEventRepository;

    @KafkaListener(topics = KafkaTopics.ORDER_APPROVED, groupId = "${spring.application.name}-approval")
    @Transactional
    public void onOrderApproved(String payload) throws Exception {
        OrderApprovedEvent event = objectMapper.readValue(payload, OrderApprovedEvent.class);
        handle(event.traceId(), event.orderId(), event.orderType(), true, null);
    }

    @KafkaListener(topics = KafkaTopics.ORDER_REJECTED, groupId = "${spring.application.name}-rejection")
    @Transactional
    public void onOrderRejected(String payload) throws Exception {
        OrderRejectedEvent event = objectMapper.readValue(payload, OrderRejectedEvent.class);
        handle(event.traceId(), event.orderId(), event.orderType(), false, event.reason());
    }

    private void handle(UUID traceId, UUID orderId, OrderType orderType, boolean approved, String reason) {
        if (processedEventRepository.existsById(traceId)) {
            log.info("Skipping duplicate event {}", traceId);
            return;
        }

        Order order = orderRepository.find(orderId).orElse(null);
        if (order == null) {
            log.warn("Order {} not found for fulfillment event", orderId);
            return;
        }

        if (approved) {
            if (orderType == OrderType.STOCK && order instanceof StockOrder stockOrder) {
                if (stockOrder.getStatus() == StockOrderStatus.PAID) {
                    stockOrder.setStatus(StockOrderStatus.READY_FOR_PICKUP);
                    orderRepository.update(stockOrder);
                }
            } else if (orderType == OrderType.CUSTOM && order instanceof CustomOrder customOrder) {
                if (customOrder.getStatus() == CustomOrderStatus.PAID) {
                    customOrder.setStatus(CustomOrderStatus.AWAITING_DELIVERY);
                    orderRepository.update(customOrder);
                }
            }
            log.info("Order {} approved by storage", orderId);
        } else {
            if (orderType == OrderType.STOCK && order instanceof StockOrder stockOrder) {
                stockOrder.setStatus(StockOrderStatus.CANCELLED);
                orderRepository.update(stockOrder);
            } else if (orderType == OrderType.CUSTOM && order instanceof CustomOrder customOrder) {
                customOrder.setStatus(CustomOrderStatus.CANCELLED);
                orderRepository.update(customOrder);
            }
            log.info("Order {} rejected by storage: {}", orderId, reason);
        }

        ProcessedEvent processed = new ProcessedEvent();
        processed.setEventId(traceId);
        processed.setProcessedAt(Instant.now());
        processedEventRepository.save(processed);
    }
}
