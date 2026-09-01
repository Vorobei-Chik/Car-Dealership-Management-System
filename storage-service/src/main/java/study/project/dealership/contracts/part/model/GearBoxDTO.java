package study.project.dealership.contracts.part.model;

import study.project.dealership.contracts.part.model.type.GearBoxTypeDTO;

import java.math.BigDecimal;
import java.util.UUID;

public record GearBoxDTO(
        UUID id,
        GearBoxTypeDTO type,
        BigDecimal price,
        int Quantity
) {}