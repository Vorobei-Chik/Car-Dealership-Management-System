package study.project.dealership.domain.assembly;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import study.project.dealership.common.messaging.OrderType;
import study.project.dealership.domain.BaseEntity;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "assembly_orders")
public class AssemblyOrder extends BaseEntity {

    @Column(name = "source_order_id", nullable = false)
    private UUID sourceOrderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_order_type", nullable = false)
    private OrderType sourceOrderType;

    @Column(name = "car_id")
    private UUID carId;

    @Column(name = "model_id")
    private UUID modelId;

    @Column(name = "engine_id")
    private UUID engineId;

    @Column(name = "gearbox_id")
    private UUID gearBoxId;

    @Column(name = "transmission_id")
    private UUID transmissionId;

    @Column(name = "wheel_id")
    private UUID wheelId;

    @Column(name = "interior_id")
    private UUID interiorId;

    @Column(name = "rudder_id")
    private UUID rudderId;

    @Column(name = "warehouse_admin_id")
    private UUID warehouseAdminId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AssemblyOrderStatus status;
}
