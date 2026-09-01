package study.project.dealership.contracts.part.model;

import java.math.BigDecimal;
import java.util.UUID;

public record WheelDTO(
        UUID id,
        BigDecimal price,
        int Quantity
) {}
