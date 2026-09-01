package study.project.dealership.domain.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;


@Getter
@Embeddable
@NoArgsConstructor
public class Money {

    @Column(name = "money", nullable = false, precision = 19, scale = 2)
    private BigDecimal value;

    public Money(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be positive: " + value);
        }
        this.value = value.stripTrailingZeros();
    }

    @Contract(" -> new")
    public static @NotNull Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    @Contract("_ -> new")
    public @NotNull Money add(@NotNull Money other) {
        return new Money(this.value.add(other.value));
    }

    @Override
    public @NotNull String toString() {
        return String.format("Money{value=%s}", value.toPlainString());
    }
}