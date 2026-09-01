package study.project.dealership.contracts.car.model;

import study.project.dealership.contracts.car.model.type.BodyTypeDTO;

import java.math.BigDecimal;
import java.util.UUID;

public record ModelDTO(
        UUID id,
        String brand,
        BodyTypeDTO type,
        BigDecimal price
) {}
