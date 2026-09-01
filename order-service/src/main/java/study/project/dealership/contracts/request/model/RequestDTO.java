package study.project.dealership.contracts.request.model;

import study.project.dealership.contracts.car.model.CarDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record RequestDTO(
        UUID id,
        UUID client,
        CarDTO car,
        LocalDateTime date
) { }
