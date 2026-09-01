package study.project.dealership.domain.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import study.project.dealership.domain.BaseEntity;
import study.project.dealership.domain.valueobject.carinfo.Color;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "order_configurations")
public class OrderConfiguration extends BaseEntity {

    @Column(name = "model_id", nullable = false)
    private UUID modelId;

    @Column(name = "engine_id", nullable = false)
    private UUID engineId;

    @Column(name = "gearbox_id", nullable = false)
    private UUID gearBoxId;

    @Column(name = "transmission_id", nullable = false)
    private UUID transmissionId;

    @Column(name = "wheel_id", nullable = false)
    private UUID wheelId;

    @Column(name = "interior_id", nullable = false)
    private UUID interiorId;

    @Column(name = "rudder_id", nullable = false)
    private UUID rudderId;

    @Embedded
    private Color color;

    @Column(name = "is_standard", nullable = false)
    private boolean standard = false;
}
