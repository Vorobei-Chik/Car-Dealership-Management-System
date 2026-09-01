package study.project.dealership.contracts.mapping;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.contracts.part.model.*;
import study.project.dealership.domain.part.*;

public final class PartMapper {

    private PartMapper() {}

    @Contract("_ -> new")
    public static @NotNull EngineDTO toDto(@NotNull Engine engine) {
        return new EngineDTO(
                engine.getId(),
                EnumMapper.toDto(engine.getFuelType()),
                ValueObjectMapper.toBigDecimal(engine.getPower()),
                ValueObjectMapper.toBigDecimal(engine.getCapacity()),
                ValueObjectMapper.toBigDecimal(engine.getPrice()),
                ValueObjectMapper.toInt(engine.getQuantity())
        );
    }

    @Contract("_ -> new")
    public static @NotNull GearBoxDTO toDto(@NotNull GearBox gearBox) {
        return new GearBoxDTO(
                gearBox.getId(),
                EnumMapper.toDto(gearBox.getType()),
                ValueObjectMapper.toBigDecimal(gearBox.getPrice()),
                ValueObjectMapper.toInt(gearBox.getQuantity())
        );
    }

    @Contract("_ -> new")
    public static @NotNull TransmissionDTO toDto(@NotNull Transmission transmission) {
        return new TransmissionDTO(
                transmission.getId(),
                EnumMapper.toDto(transmission.getVehicleDriveType()),
                ValueObjectMapper.toBigDecimal(transmission.getPrice()),
                ValueObjectMapper.toInt(transmission.getQuantity())
        );
    }

    @Contract("_ -> new")
    public static @NotNull WheelDTO toDto(@NotNull Wheel wheel) {
        return new WheelDTO(
                wheel.getId(),
                ValueObjectMapper.toBigDecimal(wheel.getPrice()),
                ValueObjectMapper.toInt(wheel.getQuantity())
        );
    }

    @Contract("_ -> new")
    public static @NotNull InteriorDTO toDto(@NotNull Interior interior) {
        return new InteriorDTO(
                interior.getId(),
                ValueObjectMapper.toBigDecimal(interior.getPrice()),
                ValueObjectMapper.toInt(interior.getQuantity())
        );
    }

    @Contract("_ -> new")
    public static @NotNull RudderDTO toDto(@NotNull Rudder rudder) {
        return new RudderDTO(
                rudder.getId(),
                ValueObjectMapper.toBigDecimal(rudder.getPrice()),
                ValueObjectMapper.toInt(rudder.getQuantity())
        );
    }

    @Contract("_ -> new")
    public static @NotNull PartDTO toBaseDto(@NotNull Part part) {
        return new PartDTO(
                part.getId(),
                ValueObjectMapper.toBigDecimal(part.getPrice()),
                ValueObjectMapper.toInt(part.getQuantity())
        );
    }
}