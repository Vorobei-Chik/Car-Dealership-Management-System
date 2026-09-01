package study.project.dealership.contracts.order.model;

import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.contracts.order.model.status.StockOrderStatusDTO;

import java.util.UUID;

public record StockOrderDTO(
        UUID id,
        UUID client,
        UUID manager,
        StockOrderStatusDTO status,
        CarDTO car
) { }
