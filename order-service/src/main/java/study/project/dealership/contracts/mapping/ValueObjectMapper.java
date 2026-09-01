package study.project.dealership.contracts.mapping;

import org.jetbrains.annotations.NotNull;
import study.project.dealership.domain.valueobject.Money;
import study.project.dealership.domain.valueobject.Quantity;
import study.project.dealership.domain.valueobject.carinfo.Color;
import study.project.dealership.domain.valueobject.engineinfo.EngineCapacity;
import study.project.dealership.domain.valueobject.engineinfo.EnginePower;

import java.math.BigDecimal;

public final class ValueObjectMapper {

    private ValueObjectMapper() {}

    public static Money toMoney(BigDecimal value) {
        return value != null ? new Money(value) : null;
    }

    public static BigDecimal toBigDecimal(Money money) {
        return money != null ? money.getValue() : null;
    }

    public static @NotNull Quantity toQuantity(int value) {
        return new Quantity(value);
    }

    public static int toInt(Quantity quantity) {
        return quantity != null ? quantity.getValue() : 0;
    }

    public static Color toColor(String value) {
        return value != null ? new Color(value) : null;
    }

    public static String toString(Color color) {
        return color != null ? color.getValue() : null;
    }

    public static EnginePower toEnginePower(BigDecimal value) {
        return value != null ? new EnginePower(value) : null;
    }

    public static BigDecimal toBigDecimal(EnginePower power) {
        return power != null ? power.getValue() : null;
    }

    public static EngineCapacity toEngineCapacity(BigDecimal value) {
        return value != null ? new EngineCapacity(value) : null;
    }

    public static BigDecimal toBigDecimal(EngineCapacity capacity) {
        return capacity != null ? capacity.getValue() : null;
    }
}