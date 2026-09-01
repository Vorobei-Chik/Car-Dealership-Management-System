package study.project.dealership.infrastructure.grpc;

import org.jetbrains.annotations.NotNull;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.contracts.car.model.ModelDTO;
import study.project.dealership.contracts.part.model.*;
import study.project.dealership.grpc.inventory.*;

import java.util.UUID;

public final class GrpcCarProtoMapper {

    private GrpcCarProtoMapper() {}

    public static @NotNull Car toProto(@NotNull CarDTO car) {
        return Car.newBuilder()
                .setId(car.id().toString())
                .setModel(toProto(car.model()))
                .setEngine(toProto(car.engine()))
                .setGearBox(toProto(car.gearBox()))
                .setTransmission(toProto(car.transmission()))
                .setWheel(toProto(car.wheel()))
                .setInterior(toProto(car.interior()))
                .setRudder(toProto(car.rudder()))
                .setColor(car.color())
                .setPrice(car.price().toPlainString())
                .setTestDriveAvailable(car.testDriveAvailable())
                .setOrdered(car.ordered())
                .build();
    }

    private static @NotNull Model toProto(@NotNull ModelDTO model) {
        return Model.newBuilder()
                .setId(model.id().toString())
                .setBrand(model.brand())
                .setBodyType(model.type().name())
                .setPrice(model.price().toPlainString())
                .build();
    }

    private static @NotNull Engine toProto(@NotNull EngineDTO engine) {
        return Engine.newBuilder()
                .setId(engine.id().toString())
                .setFuelType(engine.fuelType().name())
                .setPower(engine.power().toPlainString())
                .setCapacity(engine.capacity().toPlainString())
                .setPrice(engine.price().toPlainString())
                .setQuantity(engine.Quantity())
                .build();
    }

    private static @NotNull GearBox toProto(@NotNull GearBoxDTO gearBox) {
        return GearBox.newBuilder()
                .setId(gearBox.id().toString())
                .setType(gearBox.type().name())
                .setPrice(gearBox.price().toPlainString())
                .setQuantity(gearBox.Quantity())
                .build();
    }

    private static @NotNull Transmission toProto(@NotNull TransmissionDTO transmission) {
        return Transmission.newBuilder()
                .setId(transmission.id().toString())
                .setDriveType(transmission.vehicleDriveType().name())
                .setPrice(transmission.price().toPlainString())
                .setQuantity(transmission.Quantity())
                .build();
    }

    private static @NotNull Wheel toProto(@NotNull WheelDTO wheel) {
        return Wheel.newBuilder()
                .setId(wheel.id().toString())
                .setPrice(wheel.price().toPlainString())
                .setQuantity(wheel.Quantity())
                .build();
    }

    private static @NotNull Interior toProto(@NotNull InteriorDTO interior) {
        return Interior.newBuilder()
                .setId(interior.id().toString())
                .setPrice(interior.price().toPlainString())
                .setQuantity(interior.Quantity())
                .build();
    }

    private static @NotNull Rudder toProto(@NotNull RudderDTO rudder) {
        return Rudder.newBuilder()
                .setId(rudder.id().toString())
                .setPrice(rudder.price().toPlainString())
                .setQuantity(rudder.Quantity())
                .build();
    }

    public static @NotNull UUID parseId(@NotNull String id) {
        return UUID.fromString(id);
    }
}
