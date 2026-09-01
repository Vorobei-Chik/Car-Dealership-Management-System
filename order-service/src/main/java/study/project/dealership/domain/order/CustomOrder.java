package study.project.dealership.domain.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.domain.valueobject.orderinfo.CustomOrderStatus;

import java.util.UUID;

@Setter
@Getter
@Entity
@DiscriminatorValue("CUSTOM")
public class CustomOrder extends Order {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CustomOrderStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "configuration_id", nullable = false)
    private OrderConfiguration configuration;

    @Contract("_, _, _ -> new")
    public static @NotNull CustomOrder create(
            UUID clientId,
            UUID managerId,
            OrderConfiguration configuration
    ) {
        CustomOrder customOrder = new CustomOrder();
        customOrder.setClientId(clientId);
        customOrder.setManagerId(managerId);
        customOrder.setConfiguration(configuration);
        return customOrder;
    }
}