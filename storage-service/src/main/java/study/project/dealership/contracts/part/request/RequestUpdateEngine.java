package study.project.dealership.contracts.part.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import study.project.dealership.contracts.part.model.type.FuelTypeDTO;

@Schema(name = "RequestUpdateEngine")
public record RequestUpdateEngine(
        @NotNull UUID id,
        @NotNull FuelTypeDTO fuel,
        @NotNull @Positive BigDecimal power,
        @NotNull @Positive BigDecimal capacity,
        @NotNull @Positive BigDecimal price,
        @Min(1) int quantity
) {
}
