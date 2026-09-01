package study.project.dealership.contracts.assembly.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import study.project.dealership.common.messaging.OrderType;

import java.util.UUID;

@Schema(name = "RequestCreateAssemblyOrder")
public record RequestCreateAssemblyOrder(
        @NotNull UUID sourceOrderId,
        @NotNull OrderType sourceOrderType,
        UUID carId,
        UUID modelId,
        UUID engineId,
        UUID gearBoxId,
        UUID transmissionId,
        UUID wheelId,
        UUID interiorId,
        UUID rudderId,
        UUID warehouseAdminId,
        @NotNull String status
) {
}
