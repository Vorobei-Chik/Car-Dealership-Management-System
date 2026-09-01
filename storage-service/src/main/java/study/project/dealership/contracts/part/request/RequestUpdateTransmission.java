package study.project.dealership.contracts.part.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import study.project.dealership.contracts.part.model.type.VehicleDriveTypeDTO;

@Schema(name = "RequestUpdateTransmission")
public record RequestUpdateTransmission(
        @NotNull UUID id,
        @NotNull VehicleDriveTypeDTO vehicleDriveType,
        @NotNull @Positive BigDecimal price,
        @Min(1) int quantity
) {
}
