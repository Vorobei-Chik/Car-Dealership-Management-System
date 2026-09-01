package study.project.dealership.contracts.part.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import study.project.dealership.contracts.part.model.type.GearBoxTypeDTO;

@Schema(name = "RequestCreateGearBox")
public record RequestCreateGearBox(
        @NotNull GearBoxTypeDTO type,
        @NotNull @Positive BigDecimal price,
        @Min(1) int quantity
) {}
