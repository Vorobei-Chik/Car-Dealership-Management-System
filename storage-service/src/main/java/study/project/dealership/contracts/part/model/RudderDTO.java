package study.project.dealership.contracts.part.model;

import java.math.BigDecimal;
import java.util.UUID;

public record RudderDTO(
        UUID id,
        BigDecimal price,
        int Quantity
) {}
