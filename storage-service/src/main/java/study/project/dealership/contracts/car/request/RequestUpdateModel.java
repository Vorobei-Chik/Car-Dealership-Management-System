package study.project.dealership.contracts.car.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import study.project.dealership.contracts.car.model.type.BodyTypeDTO;

@Schema(name = "RequestUpdateModel")
public record RequestUpdateModel(
        @NotNull UUID id,
        @NotBlank String brand,
        @NotNull BodyTypeDTO bodyType,
        @NotNull BigDecimal price
) {
}
