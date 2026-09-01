package study.project.dealership.contracts.car.model;

import study.project.dealership.contracts.part.model.*;

import java.util.UUID;

public record ConfigurationDTO(
        UUID id,
        ModelDTO model,
        EngineDTO engine,
        GearBoxDTO gearBox,
        TransmissionDTO transmission,
        WheelDTO wheel,
        InteriorDTO interior,
        RudderDTO rudder,
        String color,
        boolean standard
) {}