package study.project.dealership.contracts.part.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RequestUpdatePart")
public record RequestUpdatePart(
        @NotNull UUID partId,
        @NotNull @Positive BigDecimal price,
        @Min(1) int quantity
) {}
