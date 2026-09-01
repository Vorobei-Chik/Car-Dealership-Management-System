package study.project.dealership.domain.part;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.domain.valueobject.Money;
import study.project.dealership.domain.valueobject.engineinfo.EngineCapacity;
import study.project.dealership.domain.valueobject.engineinfo.EnginePower;
import study.project.dealership.domain.valueobject.engineinfo.FuelType;

@Setter
@Getter
@Entity
@Table(name = "engines")
@DiscriminatorValue("ENGINE")
public class Engine extends Part {

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false)
    private FuelType fuelType;

    @Embedded
    private EnginePower power;

    @Embedded
    private EngineCapacity capacity;

    @Contract("_, _, _, _ -> new")
    public static @NotNull Engine create(
            FuelType fuelType,
            EnginePower power,
            EngineCapacity capacity,
            Money price
    ) {
        Engine carEngine = new Engine();
        carEngine.setFuelType(fuelType);
        carEngine.setPower(power);
        carEngine.setCapacity(capacity);
        carEngine.setPrice(price);

        return carEngine;
    }
}
