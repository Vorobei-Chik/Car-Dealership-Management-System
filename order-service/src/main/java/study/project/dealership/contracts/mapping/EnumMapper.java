package study.project.dealership.contracts.mapping;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import study.project.dealership.contracts.car.model.type.BodyTypeDTO;
import study.project.dealership.contracts.order.model.status.CustomOrderStatusDTO;
import study.project.dealership.contracts.order.model.status.StockOrderStatusDTO;
import study.project.dealership.contracts.part.model.type.FuelTypeDTO;
import study.project.dealership.contracts.part.model.type.GearBoxTypeDTO;
import study.project.dealership.contracts.part.model.type.VehicleDriveTypeDTO;
import study.project.dealership.domain.valueobject.carinfo.BodyType;
import study.project.dealership.domain.valueobject.carinfo.VehicleDriveType;
import study.project.dealership.domain.valueobject.engineinfo.FuelType;
import study.project.dealership.domain.valueobject.gearboxinfo.GearBoxType;
import study.project.dealership.domain.valueobject.orderinfo.CustomOrderStatus;
import study.project.dealership.domain.valueobject.orderinfo.StockOrderStatus;

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

    @Contract(pure = true)
    public static CustomOrderStatus toDomain(@NotNull CustomOrderStatusDTO dto) {
        return switch (dto) {
            case DRAFT -> CustomOrderStatus.DRAFT;
            case WAREHOUSE_APPROVED -> CustomOrderStatus.WAREHOUSE_APPROVED;
            case AWAITING_PAYMENT -> CustomOrderStatus.AWAITING_PAYMENT;
            case PAID -> CustomOrderStatus.PAID;
            case AWAITING_DELIVERY -> CustomOrderStatus.AWAITING_DELIVERY;
            case READY_FOR_PICKUP -> CustomOrderStatus.READY_FOR_PICKUP;
            case COMPLETED -> CustomOrderStatus.COMPLETED;
            case CANCELLED -> CustomOrderStatus.CANCELLED;
        };
    }

    @Contract(pure = true)
    public static CustomOrderStatusDTO toDto(@NotNull CustomOrderStatus status) {
        return switch (status) {
            case DRAFT -> CustomOrderStatusDTO.DRAFT;
            case WAREHOUSE_APPROVED -> CustomOrderStatusDTO.WAREHOUSE_APPROVED;
            case AWAITING_PAYMENT -> CustomOrderStatusDTO.AWAITING_PAYMENT;
            case PAID -> CustomOrderStatusDTO.PAID;
            case AWAITING_DELIVERY -> CustomOrderStatusDTO.AWAITING_DELIVERY;
            case READY_FOR_PICKUP -> CustomOrderStatusDTO.READY_FOR_PICKUP;
            case COMPLETED -> CustomOrderStatusDTO.COMPLETED;
            case CANCELLED -> CustomOrderStatusDTO.CANCELLED;
        };
    }

    @Contract(pure = true)
    public static StockOrderStatus toDomain(@NotNull StockOrderStatusDTO dto) {
        return switch (dto) {
            case DRAFT -> StockOrderStatus.DRAFT;
            case MANAGER_APPROVED -> StockOrderStatus.MANAGER_APPROVED;
            case AWAITING_PAYMENT -> StockOrderStatus.AWAITING_PAYMENT;
            case PAID -> StockOrderStatus.PAID;
            case READY_FOR_PICKUP -> StockOrderStatus.READY_FOR_PICKUP;
            case COMPLETED -> StockOrderStatus.COMPLETED;
            case CANCELLED -> StockOrderStatus.CANCELLED;
        };
    }

    @Contract(pure = true)
    public static StockOrderStatusDTO toDto(@NotNull StockOrderStatus status) {
        return switch (status) {
            case DRAFT -> StockOrderStatusDTO.DRAFT;
            case MANAGER_APPROVED -> StockOrderStatusDTO.MANAGER_APPROVED;
            case AWAITING_PAYMENT -> StockOrderStatusDTO.AWAITING_PAYMENT;
            case PAID -> StockOrderStatusDTO.PAID;
            case READY_FOR_PICKUP -> StockOrderStatusDTO.READY_FOR_PICKUP;
            case COMPLETED -> StockOrderStatusDTO.COMPLETED;
            case CANCELLED -> StockOrderStatusDTO.CANCELLED;
        };
    }
}