package study.project.dealership.contracts.order.request;

import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import study.project.dealership.contracts.order.model.status.CustomOrderStatusDTO;

@Schema(name = "RequestUpdateCustomOrder")
public record RequestUpdateCustomOrder(
        @NotNull UUID id,
        @NotNull UUID clientId,
        @NotNull UUID managerId,
        @NotNull CustomOrderStatusDTO status,
        @NotNull UUID carModelId,
        @NotNull UUID engineId,
        @NotNull UUID gearBoxId,
        @NotNull UUID transmissionId,
        @NotNull UUID wheelId,
        @NotNull UUID interiorId,
        @NotNull UUID rudderId,
        @NotBlank String color
) {
}
