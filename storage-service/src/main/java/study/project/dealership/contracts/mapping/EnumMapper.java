package study.project.dealership.contracts.mapping;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import study.project.dealership.contracts.car.model.type.BodyTypeDTO;
import study.project.dealership.contracts.part.model.type.FuelTypeDTO;
import study.project.dealership.contracts.part.model.type.GearBoxTypeDTO;
import study.project.dealership.contracts.part.model.type.VehicleDriveTypeDTO;
import study.project.dealership.domain.valueobject.carinfo.BodyType;
import study.project.dealership.domain.valueobject.carinfo.VehicleDriveType;
import study.project.dealership.domain.valueobject.engineinfo.FuelType;
import study.project.dealership.domain.valueobject.gearboxinfo.GearBoxType;
public final class EnumMapper {

    private EnumMapper() {}

    @Contract(pure = true)
    public static @Nullable BodyType toDomain(BodyTypeDTO dto) {
        if (dto == null) { return null; }

        return switch (dto) {
            case SEDAN -> BodyType.SEDAN;
            case COUPE -> BodyType.COUPE;
            case STATION_WAGON -> BodyType.STATION_WAGON;
        };
    }

    @Contract(pure = true)
    public static BodyTypeDTO toDto(@NotNull BodyType domain) {
        return switch (domain) {
            case SEDAN -> BodyTypeDTO.SEDAN;
            case COUPE -> BodyTypeDTO.COUPE;
            case STATION_WAGON -> BodyTypeDTO.STATION_WAGON;
        };
    }

    @Contract(pure = true)
    public static @Nullable FuelType toDomain(FuelTypeDTO dto) {
        if (dto == null) { return null; }

        return switch (dto) {
            case GAS -> FuelType.GAS;
            case DIESEL -> FuelType.DIESEL;
            case ELECTRIC -> FuelType.ELECTRIC;
        };
    }

    @Contract(pure = true)
    public static FuelTypeDTO toDto(@NotNull FuelType domain) {
        return switch (domain) {
            case GAS -> FuelTypeDTO.GAS;
            case DIESEL -> FuelTypeDTO.DIESEL;
            case ELECTRIC -> FuelTypeDTO.ELECTRIC;
        };
    }

    @Contract(pure = true)
    public static @Nullable GearBoxType toDomain(GearBoxTypeDTO dto) {
        if (dto == null) { return null; }

        return switch (dto) {
            case MECHANIC -> GearBoxType.MECHANIC;
            case AUTOMATIC -> GearBoxType.AUTOMATIC;
        };
    }

    @Contract(pure = true)
    public static GearBoxTypeDTO toDto(@NotNull GearBoxType domain) {
        return switch (domain) {
            case MECHANIC -> GearBoxTypeDTO.MECHANIC;
            case AUTOMATIC -> GearBoxTypeDTO.AUTOMATIC;
        };
    }

    @Contract(pure = true)
    public static @Nullable VehicleDriveType toDomain(VehicleDriveTypeDTO dto) {
        if (dto == null) { return null; }

        return switch (dto) {
            case FRONT -> VehicleDriveType.FRONT;
            case REAR -> VehicleDriveType.REAR;
            case FULL -> VehicleDriveType.FULL;
        };
    }

    @Contract(pure = true)
    public static VehicleDriveTypeDTO toDto(@NotNull VehicleDriveType domain) {
        return switch (domain) {
            case FRONT -> VehicleDriveTypeDTO.FRONT;
            case REAR -> VehicleDriveTypeDTO.REAR;
            case FULL -> VehicleDriveTypeDTO.FULL;
        };
    }
}