package study.project.dealership.common.messaging;

import java.util.UUID;

public record OrderSentForApprovalEvent(
        UUID orderId,
        OrderType orderType,
        UUID traceId,
        UUID carId,
        UUID modelId,
        UUID engineId,
        UUID gearBoxId,
        UUID transmissionId,
        UUID wheelId,
        UUID interiorId,
        UUID rudderId
) {
}
