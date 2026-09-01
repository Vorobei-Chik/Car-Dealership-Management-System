package study.project.dealership.contracts.assembly.model;

import study.project.dealership.common.messaging.OrderType;

import java.time.Instant;
import java.util.UUID;

public record AssemblyOrderDTO(
        UUID id,
        UUID sourceOrderId,
        OrderType sourceOrderType,
        UUID carId,
        UUID modelId,
        UUID engineId,
        UUID gearBoxId,
        UUID transmissionId,
        UUID wheelId,
        UUID interiorId,
        UUID rudderId,
        UUID warehouseAdminId,
        String status,
        Instant createdAt,
        Instant updatedAt,
        boolean removed
) {
}
