package study.project.dealership.domain.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Quantity {

    @Column(name = "quantity", nullable = false)
    private int value;

    public Quantity(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Quantity must be positive: " + value);
        }
        this.value = value;
    }
}
