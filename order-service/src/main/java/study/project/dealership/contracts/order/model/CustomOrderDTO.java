package study.project.dealership.contracts.order.model;

import study.project.dealership.contracts.car.model.ConfigurationDTO;
import study.project.dealership.contracts.order.model.status.CustomOrderStatusDTO;

import java.util.UUID;

public record CustomOrderDTO(
        UUID id,
        UUID client,
        UUID manager,
        CustomOrderStatusDTO status,
        ConfigurationDTO configuration
) { }
