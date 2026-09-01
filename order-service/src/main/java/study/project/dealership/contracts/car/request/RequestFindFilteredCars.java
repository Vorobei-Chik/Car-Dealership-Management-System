package study.project.dealership.contracts.car.request;

import java.math.BigDecimal;
import java.util.UUID;
import study.project.dealership.contracts.car.model.type.BodyTypeDTO;
import study.project.dealership.contracts.part.model.type.FuelTypeDTO;
import study.project.dealership.contracts.part.model.type.GearBoxTypeDTO;
import study.project.dealership.contracts.part.model.type.VehicleDriveTypeDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RequestFindFilteredCars")
public record RequestFindFilteredCars(
        UUID model,
        String color,
        String brand,
        BigDecimal maxPrice,
        BigDecimal minPrice,
        FuelTypeDTO fuelType,
        BodyTypeDTO bodyType,
        GearBoxTypeDTO gearBoxType,
        BigDecimal maxEnginePower,
        BigDecimal minEnginePower,
        BigDecimal maxEngineCapacity,
        BigDecimal minEngineCapacity,
        VehicleDriveTypeDTO vehicleDriveType
) {}
