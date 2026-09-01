package study.project.dealership.domain.part;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.domain.valueobject.Money;

@Entity
@Table(name = "rudders")
@DiscriminatorValue("RUDDER")
public class Rudder extends Part {
    @Contract("_ -> new")
    public static @NotNull Rudder create(
            Money price
    ) {
        Rudder carRudder = new Rudder();
        carRudder.setPrice(price);

        return carRudder;
    }
}
