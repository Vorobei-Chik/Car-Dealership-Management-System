package study.project.dealership.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.project.dealership.application.services.StorageInternalService;
import study.project.dealership.common.messaging.*;
import study.project.dealership.domain.assembly.AssemblyOrder;
import study.project.dealership.domain.assembly.AssemblyOrderStatus;
import study.project.dealership.infrastructure.database.repository.AssemblyOrderJpaRepository;
import study.project.dealership.infrastructure.messaging.inbox.ProcessedEvent;
import study.project.dealership.infrastructure.messaging.inbox.ProcessedEventRepository;
import study.project.dealership.infrastructure.messaging.outbox.OutboxService;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class OrderSentForApprovalListener {

    private static final Logger log = LoggerFactory.getLogger(OrderSentForApprovalListener.class);

    private final ObjectMapper objectMapper;
    private final ProcessedEventRepository processedEventRepository;
    private final AssemblyOrderJpaRepository assemblyOrderJpaRepository;
    private final StorageInternalService storageInternalService;
    private final OutboxService outboxService;

    @KafkaListener(topics = KafkaTopics.ORDER_SENT_FOR_APPROVAL, groupId = "${spring.application.name}-fulfillment")
    @Transactional
    public void onOrderSentForApproval(String payload) throws Exception {
        OrderSentForApprovalEvent event = objectMapper.readValue(payload, OrderSentForApprovalEvent.class);
        UUID traceId = event.traceId();
        if (processedEventRepository.existsById(traceId)) {
            log.info("Skipping duplicate fulfillment event {}", traceId);
            return;
        }

        AssemblyOrder assemblyOrder = new AssemblyOrder();
        assemblyOrder.setSourceOrderId(event.orderId());
        assemblyOrder.setSourceOrderType(event.orderType());
        assemblyOrder.setCarId(event.carId());
        assemblyOrder.setModelId(event.modelId());
        assemblyOrder.setEngineId(event.engineId());
        assemblyOrder.setGearBoxId(event.gearBoxId());
        assemblyOrder.setTransmissionId(event.transmissionId());
        assemblyOrder.setWheelId(event.wheelId());
        assemblyOrder.setInteriorId(event.interiorId());
        assemblyOrder.setRudderId(event.rudderId());
        assemblyOrder.setStatus(AssemblyOrderStatus.CREATED);
        assemblyOrderJpaRepository.save(assemblyOrder);

        boolean approved = storageInternalService.fulfillPaidOrder(event);
        assemblyOrder.setStatus(approved ? AssemblyOrderStatus.ASSEMBLED : AssemblyOrderStatus.FAIL);
        assemblyOrderJpaRepository.save(assemblyOrder);

        if (approved) {
            OrderApprovedEvent approvedEvent = new OrderApprovedEvent(event.orderId(), event.orderType(), traceId);
            outboxService.enqueue(KafkaTopics.ORDER_APPROVED, event.orderId().toString(), approvedEvent, traceId);
            log.info("Order {} approved by storage", event.orderId());
        } else {
            storageInternalService.releasePaidOrderReservation(event);
            OrderRejectedEvent rejectedEvent = new OrderRejectedEvent(
                    event.orderId(), event.orderType(), traceId, "Insufficient stock for assembly"
            );
            outboxService.enqueue(KafkaTopics.ORDER_REJECTED, event.orderId().toString(), rejectedEvent, traceId);
            log.info("Order {} rejected by storage", event.orderId());
        }

        ProcessedEvent processed = new ProcessedEvent();
        processed.setEventId(traceId);
        processed.setProcessedAt(Instant.now());
        processedEventRepository.save(processed);
    }
}
