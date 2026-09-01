package study.project.dealership.contracts.mapping;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.domain.car.Car;

public final class CarMapper {

    private CarMapper() {}

    @Contract("_ -> new")
    public static @NotNull CarDTO toDto(@NotNull Car car) {
        return new CarDTO(
                car.getId(),
                ModelMapper.toDto(car.getModel()),
                PartMapper.toDto(car.getEngine()),
                PartMapper.toDto(car.getGearBox()),
                PartMapper.toDto(car.getTransmission()),
                PartMapper.toDto(car.getWheel()),
                PartMapper.toDto(car.getInterior()),
                PartMapper.toDto(car.getRudder()),
                ValueObjectMapper.toString(car.getColor()),
                ValueObjectMapper.toBigDecimal(car.getPrice()),
                car.isTestDriveAvailable(),
                car.isOrdered()
        );
    }
}