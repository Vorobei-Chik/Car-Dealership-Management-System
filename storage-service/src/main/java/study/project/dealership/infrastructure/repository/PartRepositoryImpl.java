package study.project.dealership.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.project.dealership.abstractions.repository.PartRepository;
import study.project.dealership.domain.part.*;
import study.project.dealership.infrastructure.database.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PartRepositoryImpl implements PartRepository {

    private final PartJpaRepository partJpaRepository;
    private final EngineJpaRepository engineJpaRepository;
    private final GearBoxJpaRepository gearBoxJpaRepository;
    private final TransmissionJpaRepository transmissionJpaRepository;
    private final WheelJpaRepository wheelJpaRepository;
    private final InteriorJpaRepository interiorJpaRepository;
    private final RudderJpaRepository rudderJpaRepository;
    private final PartCompatibilityJpaRepository partCompatibilityJpaRepository;

    @Override
    public void add(Part part) {
        partJpaRepository.save(part);
    }

    @Override
    public void update(Part part) {
        partJpaRepository.save(part);
    }

    @Override
    public void remove(UUID id) {
        partJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Part> find(UUID id) {
        return partJpaRepository.findById(id);
    }

    @Override
    public List<Part> getAll() {
        return partJpaRepository.findAll();
    }

    @Override
    public void addCompatiblePart(UUID modelId, UUID partId) {
        PartCompatibility compatibility = new PartCompatibility();
        compatibility.setModelId(modelId);
        compatibility.setPartId(partId);
        partCompatibilityJpaRepository.save(compatibility);
    }

    @Override
    public void removeCompatiblePart(UUID modelId, UUID partId) {
        partCompatibilityJpaRepository.findByModelIdAndPartId(modelId, partId)
                .ifPresent(partCompatibilityJpaRepository::delete);
    }

    @Override
    public boolean isCompatible(UUID partId, UUID modelId) {
        return partCompatibilityJpaRepository.existsByModelIdAndPartId(modelId, partId);
    }

    @Override
    public Optional<Engine> findEngine(UUID id) {
        return engineJpaRepository.findById(id);
    }

    @Override
    public Optional<GearBox> findGearBox(UUID id) {
        return gearBoxJpaRepository.findById(id);
    }

    @Override
    public Optional<Transmission> findTransmission(UUID id) {
        return transmissionJpaRepository.findById(id);
    }

    @Override
    public Optional<Wheel> findWheel(UUID id) {
        return wheelJpaRepository.findById(id);
    }

    @Override
    public Optional<Interior> findInterior(UUID id) {
        return interiorJpaRepository.findById(id);
    }

    @Override
    public Optional<Rudder> findRudder(UUID id) {
        return rudderJpaRepository.findById(id);
    }

    @Override
    public List<Engine> getCompatibleEngines(UUID modelId) {
        if (modelId == null) {
            return engineJpaRepository.findAll();
        }

        List<UUID> partIds = partCompatibilityJpaRepository.findPartIdsByModelId(modelId);
        if (partIds.isEmpty()) {
            return List.of();
        }
        return engineJpaRepository.findAllById(partIds);
    }

    @Override
    public List<GearBox> getCompatibleGearBoxes(UUID modelId) {
        if (modelId == null) {
            return gearBoxJpaRepository.findAll();
        }

        List<UUID> partIds = partCompatibilityJpaRepository.findPartIdsByModelId(modelId);
        if (partIds.isEmpty()) {
            return List.of();
        }
        return gearBoxJpaRepository.findAllById(partIds);
    }

    @Override
    public List<Transmission> getCompatibleTransmissions(UUID modelId) {
        if (modelId == null) {
            return transmissionJpaRepository.findAll();
        }

        List<UUID> partIds = partCompatibilityJpaRepository.findPartIdsByModelId(modelId);
        if (partIds.isEmpty()) {
            return List.of();
        }
        return transmissionJpaRepository.findAllById(partIds);
    }

    @Override
    public List<Wheel> getCompatibleWheels(UUID modelId) {
        if (modelId == null) {
            return wheelJpaRepository.findAll();
        }

        List<UUID> partIds = partCompatibilityJpaRepository.findPartIdsByModelId(modelId);
        if (partIds.isEmpty()) {
            return List.of();
        }
        return wheelJpaRepository.findAllById(partIds);
    }

    @Override
    public List<Interior> getCompatibleInteriors(UUID modelId) {
        if (modelId == null) {
            return interiorJpaRepository.findAll();
        }

        List<UUID> partIds = partCompatibilityJpaRepository.findPartIdsByModelId(modelId);
        if (partIds.isEmpty()) {
            return List.of();
        }
        return interiorJpaRepository.findAllById(partIds);
    }

    @Override
    public List<Rudder> getCompatibleRudders(UUID modelId) {
        if (modelId == null) {
            return rudderJpaRepository.findAll();
        }

        List<UUID> partIds = partCompatibilityJpaRepository.findPartIdsByModelId(modelId);
        if (partIds.isEmpty()) {
            return List.of();
        }
        return rudderJpaRepository.findAllById(partIds);
    }
}