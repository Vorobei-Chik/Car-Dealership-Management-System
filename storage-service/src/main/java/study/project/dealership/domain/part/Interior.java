package study.project.dealership.domain.part;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.domain.valueobject.Money;

@Entity
@Table(name = "interiors")
@DiscriminatorValue("INTERIOR")
public class Interior extends Part {
    @Contract("_ -> new")
    public static @NotNull Interior create(
            Money price
    ) {
        Interior carInterior = new Interior();
        carInterior.setPrice(price);

        return carInterior;
    }
}
