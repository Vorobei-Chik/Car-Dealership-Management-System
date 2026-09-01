package study.project.dealership.domain.valueobject.engineinfo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Getter
@Embeddable
@NoArgsConstructor
public class EnginePower {

    @Column(name = "power", nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    public EnginePower(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Power cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Power must be positive: " + value);
        }
        this.value = value.stripTrailingZeros();
    }
}