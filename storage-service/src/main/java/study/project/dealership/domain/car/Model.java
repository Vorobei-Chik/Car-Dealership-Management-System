package study.project.dealership.domain.car;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.domain.BaseEntity;
import study.project.dealership.domain.valueobject.carinfo.BodyType;
import study.project.dealership.domain.valueobject.Money;

@Entity
@Setter
@Getter
@Table(name = "car_models")
public class Model extends BaseEntity {

    @Column(name = "brand", nullable = false)
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "body_type", nullable = false)
    private BodyType bodyType;

    @Embedded
    private Money price;

    public static @NotNull Model create(
            String brand,
            BodyType type,
            Money price
    ) {
        Model carModel = new Model();
        carModel.setBrand(brand);
        carModel.setBodyType(type);
        carModel.setPrice(price);

        return carModel;
    }
}
