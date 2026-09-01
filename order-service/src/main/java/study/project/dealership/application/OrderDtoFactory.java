package study.project.dealership.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.contracts.car.model.ConfigurationDTO;
import study.project.dealership.contracts.mapping.OrderMapper;
import study.project.dealership.contracts.order.model.CustomOrderDTO;
import study.project.dealership.contracts.order.model.StockOrderDTO;
import study.project.dealership.domain.order.CustomOrder;
import study.project.dealership.domain.order.OrderConfiguration;
import study.project.dealership.domain.order.StockOrder;
import study.project.dealership.infrastructure.client.StorageClient;

@Component
@RequiredArgsConstructor
public class OrderDtoFactory {

    private final StorageClient storageClient;

    public CustomOrderDTO toDto(CustomOrder order) {
        OrderConfiguration config = order.getConfiguration();
        ConfigurationDTO configuration = storageClient.getConfigurationSnapshot(
                config.getModelId(),
                config.getEngineId(),
                config.getGearBoxId(),
                config.getTransmissionId(),
                config.getWheelId(),
                config.getInteriorId(),
                config.getRudderId(),
                config.getColor().getValue()
        );
        return OrderMapper.toDto(order, configuration);
    }

    public StockOrderDTO toDto(StockOrder order) {
        CarDTO car = storageClient.getCar(order.getCarId());
        return OrderMapper.toDto(order, car);
    }
}
