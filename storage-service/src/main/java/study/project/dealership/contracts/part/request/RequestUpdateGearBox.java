package study.project.dealership.contracts.part.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import study.project.dealership.contracts.part.model.type.GearBoxTypeDTO;

@Schema(name = "RequestUpdateGearBox")
public record RequestUpdateGearBox(
        @NotNull UUID id,
        @NotNull GearBoxTypeDTO type,
        @NotNull @Positive BigDecimal price,
        @Min(1) int quantity
) {
}
