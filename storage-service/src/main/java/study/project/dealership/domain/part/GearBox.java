package study.project.dealership.domain.part;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.domain.valueobject.Money;
import study.project.dealership.domain.valueobject.gearboxinfo.GearBoxType;

@Setter
@Getter
@Entity
@Table(name = "gear_boxes")
@DiscriminatorValue("GEARBOX")
public class GearBox extends Part {

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private GearBoxType type;

    @Contract("_, _ -> new")
    public static @NotNull GearBox create(
            GearBoxType type,
            Money price
    ) {
        GearBox carGearBox = new GearBox();
        carGearBox.setType(type);
        carGearBox.setPrice(price);

        return carGearBox;
    }
}
