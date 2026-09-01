package study.project.dealership.contracts.order.request;

import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RequestCreateCustomOrder")
public record RequestCreateCustomOrder(
        @NotNull UUID carModelId,
        @NotNull UUID engineId,
        @NotNull UUID gearBoxId,
        @NotNull UUID transmissionId,
        @NotNull UUID wheelId,
        @NotNull UUID interiorId,
        @NotNull UUID rudderId,
        @NotBlank String color
) {}
