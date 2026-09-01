package study.project.dealership.contracts.car.request;

import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RequestCreateStandardCar")
public record RequestCreateStandardCar(
        @NotNull UUID modelId,
        @Min(1) int count
) {}
