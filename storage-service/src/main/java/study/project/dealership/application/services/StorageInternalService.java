package study.project.dealership.application.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.project.dealership.abstractions.repository.CarRepository;
import study.project.dealership.abstractions.repository.ModelRepository;
import study.project.dealership.abstractions.repository.PartRepository;
import study.project.dealership.application.exception.ConflictException;
import study.project.dealership.application.exception.NotFoundException;
import study.project.dealership.common.messaging.OrderSentForApprovalEvent;
import study.project.dealership.common.messaging.OrderType;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.contracts.car.model.ConfigurationDTO;
import study.project.dealership.contracts.mapping.CarMapper;
import study.project.dealership.contracts.mapping.ConfigurationMapper;
import study.project.dealership.contracts.mapping.ValueObjectMapper;
import study.project.dealership.contracts.order.request.RequestCreateCustomOrder;
import study.project.dealership.domain.car.Car;
import study.project.dealership.domain.car.Configuration;
import study.project.dealership.domain.car.Model;
import study.project.dealership.domain.part.*;
import study.project.dealership.domain.valueobject.Quantity;
import study.project.dealership.domain.valueobject.carinfo.Color;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageInternalService {

    private final CarRepository carRepository;
    private final ModelRepository modelRepository;
    private final PartRepository partRepository;

    @Transactional
    public void reserveCar(UUID carId) {
        Car car = carRepository.findCar(carId).orElseThrow(() -> new NotFoundException("Car not found"));
        if (car.isOrdered()) {
            throw new ConflictException("Car is already ordered");
        }
        car.setOrdered(true);
        carRepository.update(car);
    }

    @Transactional
    public void releaseCar(UUID carId) {
        Car car = carRepository.findCar(carId).orElseThrow(() -> new NotFoundException("Car not found"));
        car.setOrdered(false);
        carRepository.update(car);
    }

    @Transactional
    public void validateAndReserveCustomOrder(RequestCreateCustomOrder request) {
        Model model = modelRepository.find(request.carModelId())
                .orElseThrow(() -> new NotFoundException("Model not found"));

        requirePartWithStock(partRepository.findEngine(request.engineId()), request.engineId(), model.getId());
        requirePartWithStock(partRepository.findGearBox(request.gearBoxId()), request.gearBoxId(), model.getId());
        requirePartWithStock(partRepository.findTransmission(request.transmissionId()), request.transmissionId(), model.getId());
        requirePartWithStock(partRepository.findWheel(request.wheelId()), request.wheelId(), model.getId());
        requirePartWithStock(partRepository.findInterior(request.interiorId()), request.interiorId(), model.getId());
        requirePartWithStock(partRepository.findRudder(request.rudderId()), request.rudderId(), model.getId());
    }

    @Transactional
    public void releaseCustomOrderParts(UUID modelId, UUID engineId, UUID gearBoxId,
                                        UUID transmissionId, UUID wheelId, UUID interiorId, UUID rudderId) {
        increment(partRepository.findEngine(engineId).orElseThrow(() -> new NotFoundException("Engine not found")));
        increment(partRepository.findGearBox(gearBoxId).orElseThrow(() -> new NotFoundException("GearBox not found")));
        increment(partRepository.findTransmission(transmissionId).orElseThrow(() -> new NotFoundException("Transmission not found")));
        increment(partRepository.findWheel(wheelId).orElseThrow(() -> new NotFoundException("Wheel not found")));
        increment(partRepository.findInterior(interiorId).orElseThrow(() -> new NotFoundException("Interior not found")));
        increment(partRepository.findRudder(rudderId).orElseThrow(() -> new NotFoundException("Rudder not found")));
    }

    @Transactional(readOnly = true)
    public ConfigurationDTO configurationSnapshot(UUID modelId, UUID engineId, UUID gearBoxId,
                                                  UUID transmissionId, UUID wheelId, UUID interiorId,
                                                  UUID rudderId, String color) {
        Configuration configuration = buildConfiguration(modelId, engineId, gearBoxId, transmissionId, wheelId, interiorId, rudderId, color);
        return ConfigurationMapper.toDto(configuration);
    }

    @Transactional
    public CarDTO buildCar(ConfigurationDTO configurationDto) {
        Configuration configuration = buildConfiguration(
                configurationDto.model().id(),
                configurationDto.engine().id(),
                configurationDto.gearBox().id(),
                configurationDto.transmission().id(),
                configurationDto.wheel().id(),
                configurationDto.interior().id(),
                configurationDto.rudder().id(),
                configurationDto.color()
        );
        Car car = configuration.build();
        car.setOrdered(true);
        carRepository.add(car);
        return CarMapper.toDto(car);
    }

    @Transactional
    public boolean fulfillPaidOrder(OrderSentForApprovalEvent event) {
        if (event.orderType() == OrderType.STOCK) {
            Car car = carRepository.findCar(event.carId()).orElse(null);
            return car != null && car.isOrdered();
        }
        return tryReserveCustomOrderParts(event);
    }

    @Transactional
    public void releasePaidOrderReservation(OrderSentForApprovalEvent event) {
        if (event.orderType() == OrderType.STOCK && event.carId() != null) {
            carRepository.findCar(event.carId()).ifPresent(car -> {
                car.setOrdered(false);
                carRepository.update(car);
            });
        }
    }

    private boolean tryReserveCustomOrderParts(OrderSentForApprovalEvent event) {
        Model model = modelRepository.find(event.modelId()).orElse(null);
        if (model == null
                || event.engineId() == null
                || event.gearBoxId() == null
                || event.transmissionId() == null
                || event.wheelId() == null
                || event.interiorId() == null
                || event.rudderId() == null) {
            return false;
        }
        UUID modelId = model.getId();
        try {
            Engine engine = requirePartWithStock(partRepository.findEngine(event.engineId()), event.engineId(), modelId);
            GearBox gearBox = requirePartWithStock(partRepository.findGearBox(event.gearBoxId()), event.gearBoxId(), modelId);
            Transmission transmission = requirePartWithStock(
                    partRepository.findTransmission(event.transmissionId()), event.transmissionId(), modelId);
            Wheel wheel = requirePartWithStock(partRepository.findWheel(event.wheelId()), event.wheelId(), modelId);
            Interior interior = requirePartWithStock(partRepository.findInterior(event.interiorId()), event.interiorId(), modelId);
            Rudder rudder = requirePartWithStock(partRepository.findRudder(event.rudderId()), event.rudderId(), modelId);
            decrement(engine);
            decrement(gearBox);
            decrement(transmission);
            decrement(wheel);
            decrement(interior);
            decrement(rudder);
            return true;
        } catch (NotFoundException | ConflictException ex) {
            return false;
        }
    }

    private Configuration buildConfiguration(UUID modelId, UUID engineId, UUID gearBoxId,
                                           UUID transmissionId, UUID wheelId, UUID interiorId,
                                           UUID rudderId, String color) {
        Model model = modelRepository.find(modelId).orElseThrow(() -> new NotFoundException("Model not found"));
        Configuration configuration = new Configuration();
        configuration.setCarModel(model);
        configuration.setEngine(partRepository.findEngine(engineId).orElseThrow(() -> new NotFoundException("Engine not found")));
        configuration.setGearBox(partRepository.findGearBox(gearBoxId).orElseThrow(() -> new NotFoundException("GearBox not found")));
        configuration.setTransmission(partRepository.findTransmission(transmissionId).orElseThrow(() -> new NotFoundException("Transmission not found")));
        configuration.setWheels(partRepository.findWheel(wheelId).orElseThrow(() -> new NotFoundException("Wheel not found")));
        configuration.setInterior(partRepository.findInterior(interiorId).orElseThrow(() -> new NotFoundException("Interior not found")));
        configuration.setRudder(partRepository.findRudder(rudderId).orElseThrow(() -> new NotFoundException("Rudder not found")));
        configuration.setColor(ValueObjectMapper.toColor(color));
        configuration.setStandard(false);
        return configuration;
    }

    private <T extends Part> T requirePartWithStock(java.util.Optional<T> partOpt, UUID partId, UUID modelId) {
        T part = partOpt.orElseThrow(() -> new NotFoundException("Part not found"));
        if (!partRepository.isCompatible(partId, modelId)) {
            throw new ConflictException("Part not compatible with model");
        }
        if (part.getQuantity().getValue() < 1) {
            throw new ConflictException("Part out of stock");
        }
        return part;
    }

    private void decrement(Part part) {
        part.setQuantity(new Quantity(part.getQuantity().getValue() - 1));
        partRepository.update(part);
    }

    private void increment(Part part) {
        part.setQuantity(new Quantity(part.getQuantity().getValue() + 1));
        partRepository.update(part);
    }
}
