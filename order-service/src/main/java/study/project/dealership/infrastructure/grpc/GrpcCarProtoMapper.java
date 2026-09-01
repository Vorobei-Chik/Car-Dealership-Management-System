package study.project.dealership.infrastructure.grpc;

import org.jetbrains.annotations.NotNull;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.contracts.car.model.ModelDTO;
import study.project.dealership.contracts.car.model.type.BodyTypeDTO;
import study.project.dealership.contracts.part.model.*;
import study.project.dealership.contracts.part.model.type.FuelTypeDTO;
import study.project.dealership.contracts.part.model.type.GearBoxTypeDTO;
import study.project.dealership.contracts.part.model.type.VehicleDriveTypeDTO;
import study.project.dealership.grpc.inventory.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public final class GrpcCarProtoMapper {

    private GrpcCarProtoMapper() {}

    public static @NotNull List<CarDTO> toDtoList(@NotNull List<Car> cars) {
        return cars.stream().map(GrpcCarProtoMapper::toDto).toList();
    }

    public static @NotNull CarDTO toDto(@NotNull Car car) {
        return new CarDTO(
                UUID.fromString(car.getId()),
                toModelDto(car.getModel()),
                toEngineDto(car.getEngine()),
                toGearBoxDto(car.getGearBox()),
                toTransmissionDto(car.getTransmission()),
                toWheelDto(car.getWheel()),
                toInteriorDto(car.getInterior()),
                toRudderDto(car.getRudder()),
                car.getColor(),
                new BigDecimal(car.getPrice()),
                car.getTestDriveAvailable(),
                car.getOrdered()
        );
    }

    private static @NotNull ModelDTO toModelDto(@NotNull Model model) {
        return new ModelDTO(
                UUID.fromString(model.getId()),
                model.getBrand(),
                BodyTypeDTO.valueOf(model.getBodyType()),
                new BigDecimal(model.getPrice())
        );
    }

    private static @NotNull EngineDTO toEngineDto(@NotNull Engine engine) {
        return new EngineDTO(
                UUID.fromString(engine.getId()),
                FuelTypeDTO.valueOf(engine.getFuelType()),
                new BigDecimal(engine.getPower()),
                new BigDecimal(engine.getCapacity()),
                new BigDecimal(engine.getPrice()),
                engine.getQuantity()
        );
    }

    private static @NotNull GearBoxDTO toGearBoxDto(@NotNull GearBox gearBox) {
        return new GearBoxDTO(
                UUID.fromString(gearBox.getId()),
                GearBoxTypeDTO.valueOf(gearBox.getType()),
                new BigDecimal(gearBox.getPrice()),
                gearBox.getQuantity()
        );
    }

    private static @NotNull TransmissionDTO toTransmissionDto(@NotNull Transmission transmission) {
        return new TransmissionDTO(
                UUID.fromString(transmission.getId()),
                VehicleDriveTypeDTO.valueOf(transmission.getDriveType()),
                new BigDecimal(transmission.getPrice()),
                transmission.getQuantity()
        );
    }

    private static @NotNull WheelDTO toWheelDto(@NotNull Wheel wheel) {
        return new WheelDTO(
                UUID.fromString(wheel.getId()),
                new BigDecimal(wheel.getPrice()),
                wheel.getQuantity()
        );
    }

    private static @NotNull InteriorDTO toInteriorDto(@NotNull Interior interior) {
        return new InteriorDTO(
                UUID.fromString(interior.getId()),
                new BigDecimal(interior.getPrice()),
                interior.getQuantity()
        );
    }

    private static @NotNull RudderDTO toRudderDto(@NotNull Rudder rudder) {
        return new RudderDTO(
                UUID.fromString(rudder.getId()),
                new BigDecimal(rudder.getPrice()),
                rudder.getQuantity()
        );
    }
}
