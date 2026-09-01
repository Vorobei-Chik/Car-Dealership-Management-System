package study.project.dealership.contracts.car.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import study.project.dealership.contracts.car.model.type.BodyTypeDTO;

@Schema(name = "RequestCreateModel")
public record RequestCreateModel(
        @NotBlank String brand,
        @NotNull BodyTypeDTO bodyType,
        @NotNull BigDecimal price
) {}
