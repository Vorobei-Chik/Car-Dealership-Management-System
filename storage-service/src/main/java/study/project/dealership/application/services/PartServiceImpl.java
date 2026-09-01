package study.project.dealership.application.services;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.project.dealership.abstractions.repository.ModelRepository;
import study.project.dealership.abstractions.repository.PartRepository;
import study.project.dealership.application.exception.ConflictException;
import study.project.dealership.application.exception.NotFoundException;
import study.project.dealership.contracts.mapping.EnumMapper;
import study.project.dealership.contracts.mapping.ValueObjectMapper;
import study.project.dealership.contracts.part.PartService;
import study.project.dealership.contracts.part.request.*;
import study.project.dealership.domain.part.*;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;
    private final ModelRepository modelRepository;

    @Override
    @Transactional
    public void linkPartToModel(@NotNull RequestLinkPartToModel request) {
        UUID partId = request.partId();
        UUID modelId = request.modelId();

        if (partRepository.find(partId).isEmpty()) {
            throw new NotFoundException("Part not found");
        }
        if (modelRepository.find(modelId).isEmpty()) {
            throw new NotFoundException("Model not found");
        }
        if (partRepository.isCompatible(partId, modelId)) {
            throw new ConflictException("Part already linked to this model");
        }

        partRepository.addCompatiblePart(modelId, partId);
    }

    @Override
    @Transactional
    public void unlinkPartFromModel(@NotNull RequestUnlinkPartFromModel request) {
        UUID partId = request.partId();
        UUID modelId = request.carModelId();

        if (!partRepository.isCompatible(partId, modelId)) {
            throw new ConflictException("Part not linked to this model");
        }

        partRepository.removeCompatiblePart(modelId, partId);
    }

    @Override
    @Transactional
    public Engine createEngine(@NotNull RequestCreateEngine request) {
        Engine engine = Engine.create(
                EnumMapper.toDomain(request.fuel()),
                ValueObjectMapper.toEnginePower(request.power()),
                ValueObjectMapper.toEngineCapacity(request.capacity()),
                ValueObjectMapper.toMoney(request.price())
        );
        engine.setQuantity(ValueObjectMapper.toQuantity(request.quantity()));
        partRepository.add(engine);
        return engine;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Engine> getEnginesForModel(@NotNull RequestGetEnginesForModel request) {
        return partRepository.getCompatibleEngines(request.modelId());
    }

    @Override
    @Transactional
    public Engine updateEngine(@NotNull RequestUpdateEngine request) {
        Engine engine = (Engine) partRepository.find(request.id())
                .orElseThrow(() -> new NotFoundException("Engine not found"));

        engine.setFuelType(EnumMapper.toDomain(request.fuel()));
        engine.setPower(ValueObjectMapper.toEnginePower(request.power()));
        engine.setCapacity(ValueObjectMapper.toEngineCapacity(request.capacity()));
        engine.setPrice(ValueObjectMapper.toMoney(request.price()));
        engine.setQuantity(ValueObjectMapper.toQuantity(request.quantity()));
        partRepository.update(engine);
        return engine;
    }

    @Override
    @Transactional
    public GearBox createGearBox(@NotNull RequestCreateGearBox request) {
        GearBox gearBox = GearBox.create(
                EnumMapper.toDomain(request.type()),
                ValueObjectMapper.toMoney(request.price())
        );
        gearBox.setQuantity(ValueObjectMapper.toQuantity(request.quantity()));
        partRepository.add(gearBox);
        return gearBox;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GearBox> getGearBoxes(@NotNull RequestGetGearBoxesForModel request) {
        return partRepository.getCompatibleGearBoxes(request.modelId());
    }

    @Override
    @Transactional
    public GearBox updateGearBox(@NotNull RequestUpdateGearBox request) {
        GearBox gearBox = (GearBox) partRepository.find(request.id())
                .orElseThrow(() -> new NotFoundException("Gearbox not found"));

        gearBox.setType(EnumMapper.toDomain(request.type()));
        gearBox.setPrice(ValueObjectMapper.toMoney(request.price()));
        gearBox.setQuantity(ValueObjectMapper.toQuantity(request.quantity()));
        partRepository.update(gearBox);
        return gearBox;
    }

    @Override
    @Transactional
    public Interior createInterior(@NotNull RequestCreateInterior request) {
        Interior interior = Interior.create(ValueObjectMapper.toMoney(request.price()));
        interior.setQuantity(ValueObjectMapper.toQuantity(request.quantity()));
        partRepository.add(interior);
        return interior;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Interior> getInteriors(@NotNull RequestGetInteriorsForModel request) {
        return partRepository.getCompatibleInteriors(request.modelId());
    }

    @Override
    @Transactional
    public Interior updateInterior(@NotNull RequestUpdateInterior request) {
        Interior interior = (Interior) partRepository.find(request.id())
                .orElseThrow(() -> new NotFoundException("Interior not found"));

        interior.setPrice(ValueObjectMapper.toMoney(request.price()));
        interior.setQuantity(ValueObjectMapper.toQuantity(request.quantity()));
        partRepository.update(interior);
        return interior;
    }

    @Override
    @Transactional
    public Rudder createRudder(@NotNull RequestCreateRudder request) {
        Rudder rudder = Rudder.create(ValueObjectMapper.toMoney(request.price()));
        rudder.setQuantity(ValueObjectMapper.toQuantity(request.quantity()));
        partRepository.add(rudder);
        return rudder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rudder> getRudders(@NotNull RequestGetRuddersForModel request) {
        return partRepository.getCompatibleRudders(request.modelId());
    }

    @Override
    @Transactional
    public Rudder updateRudder(@NotNull RequestUpdateRudder request) {
        Rudder rudder = (Rudder) partRepository.find(request.id())
                .orElseThrow(() -> new NotFoundException("Rudder not found"));

        rudder.setPrice(ValueObjectMapper.toMoney(request.price()));
        rudder.setQuantity(ValueObjectMapper.toQuantity(request.quantity()));
        partRepository.update(rudder);
        return rudder;
    }

    @Override
    @Transactional
    public Transmission createTransmission(@NotNull RequestCreateTransmission request) {
        Transmission transmission = Transmission.create(
                EnumMapper.toDomain(request.vehicleDriveType()),
                ValueObjectMapper.toMoney(request.price())
        );
        transmission.setQuantity(ValueObjectMapper.toQuantity(request.quantity()));
        partRepository.add(transmission);
        return transmission;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transmission> getTransmissions(@NotNull RequestGetTransmissionsForModel request) {
        return partRepository.getCompatibleTransmissions(request.modelId());
    }

    @Override
    @Transactional
    public Transmission updateTransmission(@NotNull RequestUpdateTransmission request) {
        Transmission transmission = (Transmission) partRepository.find(request.id())
                .orElseThrow(() -> new NotFoundException("Transmission not found"));

        transmission.setVehicleDriveType(EnumMapper.toDomain(request.vehicleDriveType()));
        transmission.setPrice(ValueObjectMapper.toMoney(request.price()));
        transmission.setQuantity(ValueObjectMapper.toQuantity(request.quantity()));
        partRepository.update(transmission);
        return transmission;
    }

    @Override
    @Transactional
    public Wheel createWheel(@NotNull RequestCreateWheel request) {
        Wheel wheel = Wheel.create(ValueObjectMapper.toMoney(request.price()));
        wheel.setQuantity(ValueObjectMapper.toQuantity(request.quantity()));
        partRepository.add(wheel);
        return wheel;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Wheel> getWheels(@NotNull RequestGetWheelsForModel request) {
        return partRepository.getCompatibleWheels(request.modelId());
    }

    @Override
    @Transactional
    public Wheel updateWheel(@NotNull RequestUpdateWheel request) {
        Wheel wheel = (Wheel) partRepository.find(request.id())
                .orElseThrow(() -> new NotFoundException("Wheel not found"));

        wheel.setPrice(ValueObjectMapper.toMoney(request.price()));
        wheel.setQuantity(ValueObjectMapper.toQuantity(request.quantity()));
        partRepository.update(wheel);
        return wheel;
    }

    @Override
    @Transactional(readOnly = true)
    public Part findPart(@NotNull RequestFindPart request) {
        return partRepository.find(request.partId())
                .orElseThrow(() -> new NotFoundException("Part not found"));
    }

    @Override
    @Transactional
    public void removePart(@NotNull RequestRemovePart request) {
        UUID partId = request.partId();
        if (partRepository.find(partId).isEmpty()) {
            throw new NotFoundException("Part not found");
        }
        partRepository.remove(partId);
    }

    @Override
    @Transactional
    public Part updatePart(@NotNull RequestUpdatePart request) {
        Part part = partRepository.find(request.partId())
                .orElseThrow(() -> new NotFoundException("Part not found"));

        part.setPrice(ValueObjectMapper.toMoney(request.price()));
        part.setQuantity(ValueObjectMapper.toQuantity(request.quantity()));
        partRepository.update(part);
        return part;
    }
}
