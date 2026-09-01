package study.project.dealership.contracts.part.model;

import java.math.BigDecimal;
import java.util.UUID;

public record PartDTO (
        UUID id,
        BigDecimal price,
        int quantity
) { }
