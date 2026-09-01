package study.project.dealership.contracts.part.model;

import java.math.BigDecimal;
import java.util.UUID;

public record InteriorDTO(
        UUID id,
        BigDecimal price,
        int Quantity
) {}
