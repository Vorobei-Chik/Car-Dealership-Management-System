package study.project.dealership.contracts.order.model;

import java.util.UUID;

public record OrderDTO (
        UUID id,
        UUID client,
        UUID manager
) { }
