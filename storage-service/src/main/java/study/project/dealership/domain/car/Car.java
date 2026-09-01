package study.project.dealership.domain.car;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.domain.BaseEntity;
import study.project.dealership.domain.part.Engine;
import study.project.dealership.domain.part.GearBox;
import study.project.dealership.domain.part.Interior;
import study.project.dealership.domain.part.Rudder;
import study.project.dealership.domain.part.Transmission;
import study.project.dealership.domain.part.Wheel;
import study.project.dealership.domain.valueobject.Money;
import study.project.dealership.domain.valueobject.carinfo.Color;

@Entity
@Setter
@Getter
@Table(name = "cars")
public class Car extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private Model model;

    @ManyToOne
    @JoinColumn(name = "engine_id", nullable = false)
    private Engine engine;

    @ManyToOne
    @JoinColumn(name = "gearbox_id", nullable = false)
    private GearBox gearBox;

    @ManyToOne
    @JoinColumn(name = "transmission_id", nullable = false)
    private Transmission transmission;

    @ManyToOne
    @JoinColumn(name = "wheel_id", nullable = false)
    private Wheel wheel;

    @ManyToOne
    @JoinColumn(name = "interior_id", nullable = false)
    private Interior interior;

    @ManyToOne
    @JoinColumn(name = "rudder_id", nullable = false)
    private Rudder rudder;

    @Embedded
    private Color color;

    @Embedded
    private Money price;

    @Column(name = "test_drive_available", nullable = false)
    private boolean testDriveAvailable = false;

    @Column(name = "ordered", nullable = false)
    private boolean ordered = false;

    @Contract("_, _, _, _, _, _, _, _ -> new")
    public static @NotNull Car create(
            Model model,
            Engine engine,
            GearBox gearBox,
            Transmission transmission,
            Wheel wheels,
            Interior interior,
            Rudder rudder,
            Color color
    ) {
        Car car = new Car();
        car.setModel(model);
        car.setEngine(engine);
        car.setGearBox(gearBox);
        car.setTransmission(transmission);
        car.setWheel(wheels);
        car.setInterior(interior);
        car.setRudder(rudder);
        car.setColor(color);
        car.setPrice(
                sum(
                        model,
                        engine,
                        gearBox,
                        transmission,
                        wheels,
                        interior,
                        rudder
                )
        );

        return car;
    }

    private static @NotNull Money sum(
            @NotNull Model model,
            @NotNull Engine engine,
            @NotNull GearBox gearBox,
            @NotNull Transmission transmission,
            @NotNull Wheel wheels,
            @NotNull Interior interior,
            @NotNull Rudder rudder
    ) {
        Money total = Money.zero();
        total = total.add(model.getPrice())
                .add(engine.getPrice())
                .add(gearBox.getPrice())
                .add(transmission.getPrice())
                .add(wheels.getPrice())
                .add(interior.getPrice())
                .add(rudder.getPrice());

        return total;
    }
}
