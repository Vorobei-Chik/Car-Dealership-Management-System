package study.project.dealership.infrastructure.database.specification;

import jakarta.persistence.criteria.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import study.project.dealership.domain.car.Car;
import study.project.dealership.domain.car.Model;
import study.project.dealership.domain.part.Engine;
import study.project.dealership.domain.part.GearBox;
import study.project.dealership.domain.part.Transmission;
import study.project.dealership.domain.valueobject.Money;
import study.project.dealership.domain.valueobject.carinfo.BodyType;
import study.project.dealership.domain.valueobject.carinfo.VehicleDriveType;
import study.project.dealership.domain.valueobject.engineinfo.EngineCapacity;
import study.project.dealership.domain.valueobject.engineinfo.EnginePower;
import study.project.dealership.domain.valueobject.engineinfo.FuelType;
import study.project.dealership.domain.valueobject.gearboxinfo.GearBoxType;

import java.util.UUID;

public final class CarSpecifications {

    private CarSpecifications() {}

    public static Specification<Car> byModelId(UUID modelId) {
        if (modelId == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("model").get("id"), modelId);
    }

    public static Specification<Car> byColor(String color) {
        if (color == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("color").get("value"), color);
    }

    public static Specification<Car> byBrand(String brand) {
        if (brand == null) {
            return null;
        }
        return (root, query, cb) -> {
            Join<Car, Model> modelJoin = root.join("model");
            return cb.equal(modelJoin.get("brand"), brand);
        };
    }

    public static Specification<Car> withMinPrice(Money minPrice) {
        if (minPrice == null) {
            return null;
        }
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("price").get("value"), minPrice.getValue());
    }

    public static Specification<Car> withMaxPrice(Money maxPrice) {
        if (maxPrice == null) {
            return null;
        }
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("price").get("value"), maxPrice.getValue());
    }

    public static Specification<Car> byFuelType(FuelType fuelType) {
        if (fuelType == null) {
            return null;
        }
        return (root, query, cb) -> {
            Join<Car, Engine> engineJoin = root.join("engine");
            return cb.equal(engineJoin.get("fuelType"), fuelType);
        };
    }

    public static Specification<Car> byBodyType(BodyType bodyType) {
        if (bodyType == null) {
            return null;
        }
        return (root, query, cb) -> {
            Join<Car, Model> modelJoin = root.join("model");
            return cb.equal(modelJoin.get("bodyType"), bodyType);
        };
    }

    public static Specification<Car> byGearBoxType(GearBoxType gearBoxType) {
        if (gearBoxType == null) {
            return null;
        }
        return (root, query, cb) -> {
            Join<Car, GearBox> gearBoxJoin = root.join("gearBox");
            return cb.equal(gearBoxJoin.get("type"), gearBoxType);
        };
    }

    public static Specification<Car> withMinEnginePower(EnginePower minEnginePower) {
        if (minEnginePower == null) {
            return null;
        }
        return (root, query, cb) -> {
            Join<Car, Engine> engineJoin = root.join("engine");
            return cb.greaterThanOrEqualTo(engineJoin.get("power").get("value"), minEnginePower.getValue());
        };
    }

    public static Specification<Car> withMaxEnginePower(EnginePower maxEnginePower) {
        if (maxEnginePower == null) {
            return null;
        }
        return (root, query, cb) -> {
            Join<Car, Engine> engineJoin = root.join("engine");
            return cb.lessThanOrEqualTo(engineJoin.get("power").get("value"), maxEnginePower.getValue());
        };
    }

    public static Specification<Car> withMinEngineCapacity(EngineCapacity minEngineCapacity) {
        if (minEngineCapacity == null) {
            return null;
        }
        return (root, query, cb) -> {
            Join<Car, Engine> engineJoin = root.join("engine");
            return cb.greaterThanOrEqualTo(engineJoin.get("capacity").get("value"), minEngineCapacity.getValue());
        };
    }

    public static Specification<Car> withMaxEngineCapacity(EngineCapacity maxEngineCapacity) {
        if (maxEngineCapacity == null) {
            return null;
        }
        return (root, query, cb) -> {
            Join<Car, Engine> engineJoin = root.join("engine");
            return cb.lessThanOrEqualTo(engineJoin.get("capacity").get("value"), maxEngineCapacity.getValue());
        };
    }

    public static Specification<Car> byVehicleDriveType(VehicleDriveType vehicleDriveType) {
        if (vehicleDriveType == null) {
            return null;
        }
        return (root, query, cb) -> {
            Join<Car, Transmission> transmissionJoin = root.join("transmission");
            return cb.equal(transmissionJoin.get("vehicleDriveType"), vehicleDriveType);
        };
    }

    @Contract(pure = true)
    public static @NotNull Specification<Car> notRemoved() {
        return (root, query, cb) -> cb.isFalse(root.get("removed"));
    }
}