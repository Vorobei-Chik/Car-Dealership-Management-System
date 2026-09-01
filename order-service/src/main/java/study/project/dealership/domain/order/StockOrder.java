package study.project.dealership.domain.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.domain.valueobject.orderinfo.StockOrderStatus;

import java.util.UUID;

@Entity
@Setter
@Getter
@DiscriminatorValue("STOCK")
public class StockOrder extends Order {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StockOrderStatus status;

    @Column(name = "car_id", nullable = false)
    private UUID carId;

    @Contract("_, _, _ -> new")
    public static @NotNull StockOrder create(
            UUID clientId,
            UUID managerId,
            UUID carId
    ) {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setClientId(clientId);
        stockOrder.setManagerId(managerId);
        stockOrder.setCarId(carId);
        return stockOrder;
    }
}