package study.project.dealership.contracts.car.request;

import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RequestUpdateStandardConfiguration")
public record RequestUpdateStandardConfiguration(
        @NotNull UUID modelId,
        @NotNull UUID engineId,
        @NotNull UUID gearBoxId,
        @NotNull UUID transmissionId,
        @NotNull UUID wheelId,
        @NotNull UUID interiorId,
        @NotNull UUID rudderId,
        @NotBlank String color
) {}
