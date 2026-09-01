package study.project.dealership.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import study.project.dealership.abstractions.repository.CarRepository;
import study.project.dealership.domain.car.Car;
import study.project.dealership.domain.valueobject.Money;
import study.project.dealership.domain.valueobject.carinfo.BodyType;
import study.project.dealership.domain.valueobject.carinfo.VehicleDriveType;
import study.project.dealership.domain.valueobject.engineinfo.EngineCapacity;
import study.project.dealership.domain.valueobject.engineinfo.EnginePower;
import study.project.dealership.domain.valueobject.engineinfo.FuelType;
import study.project.dealership.domain.valueobject.gearboxinfo.GearBoxType;
import study.project.dealership.infrastructure.database.repository.CarJpaRepository;
import study.project.dealership.infrastructure.database.specification.CarSpecifications;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class CarRepositoryImpl implements CarRepository {

    private final CarJpaRepository carJpaRepository;

    @Override
    public void add(Car car) {
        carJpaRepository.save(car);
    }

    @Override
    public void update(Car car) {
        carJpaRepository.save(car);
    }

    @Override
    public void remove(UUID id) {
        carJpaRepository.deleteById(id);
    }

    @Override
    public void addToTestDrive(UUID id) {
        carJpaRepository.findById(id).ifPresent(car -> {
            car.setTestDriveAvailable(true);
            carJpaRepository.save(car);
        });
    }

    @Override
    public void removeFromTestDrive(UUID id) {
        carJpaRepository.findById(id).ifPresent(car -> {
            car.setTestDriveAvailable(false);
            carJpaRepository.save(car);
        });
    }

    @Override
    public Optional<Car> findCar(UUID id) {
        return carJpaRepository.findById(id);
    }

    @Override
    public List<UUID> findFilteredCars(
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
    ) {
        Specification<Car> spec = Stream.of(
                        CarSpecifications.byModelId(model),
                        CarSpecifications.byColor(color),
                        CarSpecifications.byBrand(brand),
                        CarSpecifications.withMinPrice(minPrice),
                        CarSpecifications.withMaxPrice(maxPrice),
                        CarSpecifications.byFuelType(fuelType),
                        CarSpecifications.byBodyType(bodyType),
                        CarSpecifications.byGearBoxType(gearBoxType),
                        CarSpecifications.withMinEnginePower(minEnginePower),
                        CarSpecifications.withMaxEnginePower(maxEnginePower),
                        CarSpecifications.withMinEngineCapacity(minEngineCapacity),
                        CarSpecifications.withMaxEngineCapacity(maxEngineCapacity),
                        CarSpecifications.byVehicleDriveType(vehicleDriveType),
                        CarSpecifications.notRemoved()
                )
                .filter(Objects::nonNull)
                .reduce(Specification::and)
                .orElse(Specification.unrestricted());

        return carJpaRepository.findAll(spec).stream()
                .map(Car::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> getAllCars() {
        return carJpaRepository.findAll();
    }
}