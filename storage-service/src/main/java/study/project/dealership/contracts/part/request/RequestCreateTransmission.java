package study.project.dealership.contracts.part.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import study.project.dealership.contracts.part.model.type.VehicleDriveTypeDTO;

@Schema(name = "RequestCreateTransmission")
public record RequestCreateTransmission(
        @NotNull VehicleDriveTypeDTO vehicleDriveType,
        @NotNull @Positive BigDecimal price,
        @Min(1) int quantity
) {}
