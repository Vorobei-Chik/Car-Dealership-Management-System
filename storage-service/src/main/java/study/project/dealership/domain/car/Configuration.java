package study.project.dealership.domain.car;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.project.dealership.domain.BaseEntity;
import study.project.dealership.domain.part.Engine;
import study.project.dealership.domain.part.GearBox;
import study.project.dealership.domain.part.Interior;
import study.project.dealership.domain.part.Rudder;
import study.project.dealership.domain.part.Transmission;
import study.project.dealership.domain.part.Wheel;
import study.project.dealership.domain.valueobject.carinfo.Color;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "configurations")
public class Configuration extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private Model carModel;

    @ManyToOne
    @JoinColumn(name = "engine_id")
    private Engine engine;

    @ManyToOne
    @JoinColumn(name = "gearbox_id")
    private GearBox gearBox;

    @ManyToOne
    @JoinColumn(name = "transmission_id")
    private Transmission transmission;

    @ManyToOne
    @JoinColumn(name = "wheel_id")
    private Wheel wheels;

    @ManyToOne
    @JoinColumn(name = "interior_id")
    private Interior interior;

    @ManyToOne
    @JoinColumn(name = "rudder_id")
    private Rudder rudder;

    @Embedded
    private Color color;

    @Column(name = "is_standard", nullable = false)
    private boolean standard = false;

    public Car build() {
        validateAllParts();
        return Car.create(
                carModel,
                engine,
                gearBox,
                transmission,
                wheels,
                interior,
                rudder,
                color
        );
    }

    private void validateAllParts() {
        if (carModel == null) throw new IllegalStateException("CarModel is required");
        if (engine == null) throw new IllegalStateException("Engine is required");
        if (gearBox == null) throw new IllegalStateException("GearBox is required");
        if (transmission == null) throw new IllegalStateException("Transmission is required");
        if (wheels == null) throw new IllegalStateException("Wheels are required");
        if (interior == null) throw new IllegalStateException("Interior is required");
        if (rudder == null) throw new IllegalStateException("Rudder is required");
        if (color == null) throw new IllegalStateException("Color is required");
    }
}
