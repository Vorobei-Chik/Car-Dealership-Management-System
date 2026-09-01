package study.project.dealership.abstractions.repository;

import study.project.dealership.domain.car.Car;
import study.project.dealership.domain.valueobject.Money;
import study.project.dealership.domain.valueobject.carinfo.BodyType;
import study.project.dealership.domain.valueobject.carinfo.VehicleDriveType;
import study.project.dealership.domain.valueobject.engineinfo.EngineCapacity;
import study.project.dealership.domain.valueobject.engineinfo.EnginePower;
import study.project.dealership.domain.valueobject.engineinfo.FuelType;
import study.project.dealership.domain.valueobject.gearboxinfo.GearBoxType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository {

    void add(Car car);
    void update(Car car);
    void remove(UUID id);

    void addToTestDrive(UUID id);
    void removeFromTestDrive(UUID id);

    Optional<Car> findCar(UUID id);
    List<UUID> findFilteredCars(
            UUID model,
            String color,
            String brand,
            Money maxPrice,
            Money minPrice,
            FuelType fuelType,
            BodyType bodyType,
            GearBoxType gearBoxType,
            EnginePower maxEnginePower,
            EnginePower minEnginePower,
            EngineCapacity maxEngineCapacity,
            EngineCapacity minEngineCapacity,
            VehicleDriveType vehicleDriveType
    );

    List<Car> getAllCars();
}