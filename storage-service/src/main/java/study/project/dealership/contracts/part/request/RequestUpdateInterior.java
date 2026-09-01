package study.project.dealership.contracts.part.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RequestUpdateInterior")
public record RequestUpdateInterior(
        @NotNull UUID id,
        @NotNull @Positive BigDecimal price,
        @Min(1) int quantity
) {
}
