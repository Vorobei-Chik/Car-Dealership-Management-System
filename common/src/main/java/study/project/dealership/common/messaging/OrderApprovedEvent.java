package study.project.dealership.common.messaging;

import java.util.UUID;

public record OrderApprovedEvent(
        UUID orderId,
        OrderType orderType,
        UUID traceId
) {
}
