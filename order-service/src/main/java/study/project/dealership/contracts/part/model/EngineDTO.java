package study.project.dealership.contracts.part.model;

import study.project.dealership.contracts.part.model.type.FuelTypeDTO;

import java.math.BigDecimal;
import java.util.UUID;

public record EngineDTO (
        UUID id,
        FuelTypeDTO fuelType,
        BigDecimal power,
        BigDecimal capacity,
        BigDecimal price,
        int Quantity
) {}