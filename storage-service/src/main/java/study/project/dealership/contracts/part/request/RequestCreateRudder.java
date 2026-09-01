package study.project.dealership.contracts.part.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RequestCreateRudder")
public record RequestCreateRudder(
        @NotNull @Positive BigDecimal price,
        @Min(1) int quantity
) {}
