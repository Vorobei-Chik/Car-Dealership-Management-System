package study.project.dealership.contracts.part.model;

import study.project.dealership.contracts.part.model.type.VehicleDriveTypeDTO;

import java.math.BigDecimal;
import java.util.UUID;

public record TransmissionDTO(
        UUID id,
        VehicleDriveTypeDTO vehicleDriveType,
        BigDecimal price,
        int Quantity
) {}
