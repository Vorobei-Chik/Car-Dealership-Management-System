package study.project.dealership.domain.part;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.domain.valueobject.Money;

@Entity
@Getter
@Setter
@Table(name = "wheels")
@DiscriminatorValue("WHEEL")
public class Wheel extends Part {
    @Contract("_ -> new")
    public static @NotNull Wheel create(
            Money price
    ) {
        Wheel carWheel = new Wheel();
        carWheel.setPrice(price);

        return carWheel;
    }
}
