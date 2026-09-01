package study.project.dealership.application.services;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.project.dealership.abstractions.repository.*;
import study.project.dealership.application.exception.ConflictException;
import study.project.dealership.application.exception.NotFoundException;
import study.project.dealership.contracts.car.CarService;
import study.project.dealership.contracts.car.request.*;
import study.project.dealership.contracts.mapping.EnumMapper;
import study.project.dealership.contracts.mapping.ValueObjectMapper;
import study.project.dealership.domain.car.Car;
import study.project.dealership.domain.car.Configuration;
import study.project.dealership.domain.car.Model;
import study.project.dealership.domain.part.*;
import study.project.dealership.domain.valueobject.carinfo.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ConfigurationRepository configurationRepository;
    private final ModelRepository modelRepository;
    private final PartRepository partRepository;

    @Override
    @Transactional
    public Car createCar(@NotNull RequestCreateCar request) {
        Model model = modelRepository.find(request.modelId())
                .orElseThrow(() -> new NotFoundException("Model not found"));

        Engine engine = requireCompatibleEngine(request.engineId(), model);
        GearBox gearBox = requireCompatibleGearBox(request.gearBoxId(), model);
        Transmission transmission = requireCompatibleTransmission(request.transmissionId(), model);
        Wheel wheel = requireCompatibleWheel(request.wheelId(), model);
        Interior interior = requireCompatibleInterior(request.interiorId(), model);
        Rudder rudder = requireCompatibleRudder(request.rudderId(), model);

        Color color = ValueObjectMapper.toColor(request.color());
        Car car = Car.create(model, engine, gearBox, transmission, wheel, interior, rudder, color);
        carRepository.add(car);
        return car;
    }

    @Override
    @Transactional
    public List<Car> createStandardCar(@NotNull RequestCreateStandardCar request) {
        Configuration config = configurationRepository.findByModelId(request.modelId())
                .filter(Configuration::isStandard)
                .orElseThrow(() -> new NotFoundException("Standard configuration not found for model"));

        List<Car> cars = new ArrayList<>();
        for (int i = 0; i < request.count(); i++) {
            Car car = config.build();
            carRepository.add(car);
            cars.add(car);
        }
        return cars;
    }

    @Override
    @Transactional(readOnly = true)
    public Car findCar(@NotNull RequestFindCar request) {
        return carRepository.findCar(request.id())
                .orElseThrow(() -> new NotFoundException("Car not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Car> findFilteredCars(@NotNull RequestFindFilteredCars request) {
        List<UUID> carIds = carRepository.findFilteredCars(
                request.model(),
                request.color(),
                request.brand(),
                ValueObjectMapper.toMoney(request.maxPrice()),
                ValueObjectMapper.toMoney(request.minPrice()),
                EnumMapper.toDomain(request.fuelType()),
                EnumMapper.toDomain(request.bodyType()),
                EnumMapper.toDomain(request.gearBoxType()),
                ValueObjectMapper.toEnginePower(request.maxEnginePower()),
                ValueObjectMapper.toEnginePower(request.minEnginePower()),
                ValueObjectMapper.toEngineCapacity(request.maxEngineCapacity()),
                ValueObjectMapper.toEngineCapacity(request.minEngineCapacity()),
                EnumMapper.toDomain(request.vehicleDriveType())
        );

        return carIds.stream()
                .map(carRepository::findCar)
                .flatMap(java.util.Optional::stream)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Car> getAllCars() {
        return carRepository.getAllCars();
    }

    @Override
    @Transactional
    public void removeCar(@NotNull RequestRemoveCar request) {
        UUID carId = request.carId();
        if (carRepository.findCar(carId).isEmpty()) {
            throw new NotFoundException("Car not found");
        }
        carRepository.remove(carId);
    }

    @Override
    @Transactional
    public Car updateCar(@NotNull RequestUpdateCar request) {
        Car car = carRepository.findCar(request.carId())
                .orElseThrow(() -> new NotFoundException("Car not found"));

        Model model = car.getModel();

        car.setEngine(requireCompatibleEngine(request.engineId(), model));
        car.setGearBox(requireCompatibleGearBox(request.gearBoxId(), model));
        car.setTransmission(requireCompatibleTransmission(request.transmissionId(), model));
        car.setWheel(requireCompatibleWheel(request.wheelId(), model));
        car.setInterior(requireCompatibleInterior(request.interiorId(), model));
        car.setRudder(requireCompatibleRudder(request.rudderId(), model));
        car.setColor(ValueObjectMapper.toColor(request.color()));

        carRepository.update(car);
        return car;
    }

    @Override
    @Transactional
    public Configuration createStandardConfiguration(@NotNull RequestCreateStandardConfiguration request) {
        Model model = modelRepository.find(request.modelId())
                .orElseThrow(() -> new NotFoundException("Model not found"));

        Engine engine = requireCompatibleEngine(request.engineId(), model);
        GearBox gearBox = requireCompatibleGearBox(request.gearBoxId(), model);
        Transmission transmission = requireCompatibleTransmission(request.transmissionId(), model);
        Wheel wheel = requireCompatibleWheel(request.wheelId(), model);
        Interior interior = requireCompatibleInterior(request.interiorId(), model);
        Rudder rudder = requireCompatibleRudder(request.rudderId(), model);

        Configuration config = new Configuration();
        config.setCarModel(model);
        config.setEngine(engine);
        config.setGearBox(gearBox);
        config.setTransmission(transmission);
        config.setWheels(wheel);
        config.setInterior(interior);
        config.setRudder(rudder);
        config.setColor(ValueObjectMapper.toColor(request.color()));
        config.setStandard(true);

        configurationRepository.add(config);
        return config;
    }

    @Override
    @Transactional(readOnly = true)
    public Configuration findStandardConfiguration(@NotNull RequestFindStandardConfiguration request) {
        return configurationRepository.find(request.id())
                .filter(Configuration::isStandard)
                .orElseThrow(() -> new NotFoundException("Standard configuration not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Configuration findStandardConfigurationByModel(@NotNull RequestFindStandardConfigurationByModel request) {
        return configurationRepository.findByModelId(request.modelId())
                .filter(Configuration::isStandard)
                .orElseThrow(() -> new NotFoundException("Standard configuration not found for model"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Configuration> getAllStandardConfigurations() {
        return configurationRepository.getAll();
    }

    @Override
    @Transactional
    public void removeStandardConfiguration(@NotNull RequestRemoveStandardConfiguration request) {
        Configuration config = configurationRepository.find(request.id())
                .filter(Configuration::isStandard)
                .orElseThrow(() -> new NotFoundException("Standard configuration not found"));
        configurationRepository.remove(config.getId());
    }

    @Override
    @Transactional
    public Configuration updateStandardConfiguration(@NotNull RequestUpdateStandardConfiguration request) {
        Configuration config = configurationRepository.findByModelId(request.modelId())
                .filter(Configuration::isStandard)
                .orElseThrow(() -> new NotFoundException("Standard configuration not found"));

        Model model = config.getCarModel();

        config.setEngine(requireCompatibleEngine(request.engineId(), model));
        config.setGearBox(requireCompatibleGearBox(request.gearBoxId(), model));
        config.setTransmission(requireCompatibleTransmission(request.transmissionId(), model));
        config.setWheels(requireCompatibleWheel(request.wheelId(), model));
        config.setInterior(requireCompatibleInterior(request.interiorId(), model));
        config.setRudder(requireCompatibleRudder(request.rudderId(), model));
        config.setColor(ValueObjectMapper.toColor(request.color()));

        configurationRepository.update(config);
        return config;
    }

    @Override
    @Transactional
    public Model createModel(@NotNull RequestCreateModel request) {
        Model model = Model.create(
                request.brand(),
                EnumMapper.toDomain(request.bodyType()),
                ValueObjectMapper.toMoney(request.price())
        );
        modelRepository.add(model);
        return model;
    }

    @Override
    @Transactional(readOnly = true)
    public Model findModel(@NotNull RequestFindModel request) {
        return modelRepository.find(request.id())
                .orElseThrow(() -> new NotFoundException("Model not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Model> getAllModels() {
        return modelRepository.getAll();
    }

    @Override
    @Transactional
    public void removeModel(@NotNull RequestRemoveModel request) {
        UUID id = request.id();
        if (modelRepository.find(id).isEmpty()) {
            throw new NotFoundException("Model not found");
        }
        modelRepository.remove(id);
    }

    @Override
    @Transactional
    public Model updateModel(@NotNull RequestUpdateModel request) {
        Model model = modelRepository.find(request.id())
                .orElseThrow(() -> new NotFoundException("Model not found"));

        model.setBrand(request.brand());
        model.setBodyType(EnumMapper.toDomain(request.bodyType()));
        model.setPrice(ValueObjectMapper.toMoney(request.price()));

        modelRepository.update(model);
        return model;
    }

    @Override
    @Transactional
    public Car addCarToTestDrive(@NotNull RequestAddCarToTestDrive request) {
        Car car = carRepository.findCar(request.carId())
                .orElseThrow(() -> new NotFoundException("Car not found"));

        if (car.isOrdered()) {
            throw new ConflictException("Car is already ordered and cannot be added to test drive");
        }

        carRepository.addToTestDrive(request.carId());
        return car;
    }

    @Override
    @Transactional
    public void removeCarFromTestDrive(@NotNull RequestRemoveCarFromTestDrive request) {
        if (carRepository.findCar(request.carId()).isEmpty()) {
            throw new NotFoundException("Car not found");
        }
        carRepository.removeFromTestDrive(request.carId());
    }

    private Engine requireCompatibleEngine(UUID engineId, Model model) {
        Engine engine = partRepository.findEngine(engineId)
                .orElseThrow(() -> new NotFoundException("Engine not found"));
        if (!partRepository.isCompatible(engine.getId(), model.getId())) {
            throw new ConflictException("Engine not compatible with model");
        }
        return engine;
    }

    private GearBox requireCompatibleGearBox(UUID gearBoxId, Model model) {
        GearBox gearBox = partRepository.findGearBox(gearBoxId)
                .orElseThrow(() -> new NotFoundException("GearBox not found"));
        if (!partRepository.isCompatible(gearBox.getId(), model.getId())) {
            throw new ConflictException("GearBox not compatible with model");
        }
        return gearBox;
    }

    private Transmission requireCompatibleTransmission(UUID transmissionId, Model model) {
        Transmission transmission = partRepository.findTransmission(transmissionId)
                .orElseThrow(() -> new NotFoundException("Transmission not found"));
        if (!partRepository.isCompatible(transmission.getId(), model.getId())) {
            throw new ConflictException("Transmission not compatible with model");
        }
        return transmission;
    }

    private Wheel requireCompatibleWheel(UUID wheelId, Model model) {
        Wheel wheel = partRepository.findWheel(wheelId)
                .orElseThrow(() -> new NotFoundException("Wheel not found"));
        if (!partRepository.isCompatible(wheel.getId(), model.getId())) {
            throw new ConflictException("Wheel not compatible with model");
        }
        return wheel;
    }

    private Interior requireCompatibleInterior(UUID interiorId, Model model) {
        Interior interior = partRepository.findInterior(interiorId)
                .orElseThrow(() -> new NotFoundException("Interior not found"));
        if (!partRepository.isCompatible(interior.getId(), model.getId())) {
            throw new ConflictException("Interior not compatible with model");
        }
        return interior;
    }

    private Rudder requireCompatibleRudder(UUID rudderId, Model model) {
        Rudder rudder = partRepository.findRudder(rudderId)
                .orElseThrow(() -> new NotFoundException("Rudder not found"));
        if (!partRepository.isCompatible(rudder.getId(), model.getId())) {
            throw new ConflictException("Rudder not compatible with model");
        }
        return rudder;
    }
}
