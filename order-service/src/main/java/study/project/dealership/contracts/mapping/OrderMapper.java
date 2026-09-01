package study.project.dealership.contracts.mapping;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.contracts.order.model.CustomOrderDTO;
import study.project.dealership.contracts.order.model.OrderDTO;
import study.project.dealership.contracts.order.model.StockOrderDTO;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.contracts.car.model.ConfigurationDTO;
import study.project.dealership.domain.order.CustomOrder;
import study.project.dealership.domain.order.Order;
import study.project.dealership.domain.order.StockOrder;

public final class OrderMapper {

    private OrderMapper() {}

    @Contract("_, _ -> new")
    public static @NotNull CustomOrderDTO toDto(@NotNull CustomOrder order, @NotNull ConfigurationDTO configuration) {
        return new CustomOrderDTO(
                order.getId(),
                order.getClientId(),
                order.getManagerId(),
                EnumMapper.toDto(order.getStatus()),
                configuration
        );
    }

    @Contract("_, _ -> new")
    public static @NotNull StockOrderDTO toDto(@NotNull StockOrder order, @NotNull CarDTO car) {
        return new StockOrderDTO(
                order.getId(),
                order.getClientId(),
                order.getManagerId(),
                EnumMapper.toDto(order.getStatus()),
                car
        );
    }

    public static @NotNull OrderDTO toBaseDto(@NotNull Order order) {
        return new OrderDTO(
                order.getId(),
                order.getClientId(),
                order.getManagerId()
        );
    }
}