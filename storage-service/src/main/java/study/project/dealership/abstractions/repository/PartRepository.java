package study.project.dealership.abstractions.repository;

import study.project.dealership.domain.part.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PartRepository {
    void add(Part part);
    void update(Part part);
    void remove(UUID id);
    Optional<Part> find(UUID id);
    List<Part> getAll();

    void addCompatiblePart(UUID modelId, UUID partId);
    void removeCompatiblePart(UUID modelId, UUID partId);
    boolean isCompatible(UUID partId, UUID modelId);

    Optional<Engine> findEngine(UUID id);
    Optional<GearBox> findGearBox(UUID id);
    Optional<Transmission> findTransmission(UUID id);
    Optional<Wheel> findWheel(UUID id);
    Optional<Interior> findInterior(UUID id);
    Optional<Rudder> findRudder(UUID id);

    List<Engine> getCompatibleEngines(UUID modelId);
    List<GearBox> getCompatibleGearBoxes(UUID modelId);
    List<Transmission> getCompatibleTransmissions(UUID modelId);
    List<Wheel> getCompatibleWheels(UUID modelId);
    List<Interior> getCompatibleInteriors(UUID modelId);
    List<Rudder> getCompatibleRudders(UUID modelId);
}