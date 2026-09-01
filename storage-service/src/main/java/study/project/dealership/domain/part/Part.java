package study.project.dealership.domain.part;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import study.project.dealership.domain.BaseEntity;
import study.project.dealership.domain.valueobject.Money;
import study.project.dealership.domain.valueobject.Quantity;

@Entity
@Setter
@Getter
@Table(name = "parts")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "part_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Part extends BaseEntity {

    @Embedded
    private Money price;

    @Embedded
    private Quantity quantity = new Quantity(0);
}
