package study.project.dealership.common.messaging;

import java.util.UUID;

public record OrderRejectedEvent(
        UUID orderId,
        OrderType orderType,
        UUID traceId,
        String reason
) {
}
