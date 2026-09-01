package study.project.dealership.contracts.car.model;

import study.project.dealership.contracts.part.model.*;

import java.math.BigDecimal;
import java.util.UUID;

public record CarDTO(
        UUID id,
        ModelDTO model,
        EngineDTO engine,
        GearBoxDTO gearBox,
        TransmissionDTO transmission,
        WheelDTO wheel,
        InteriorDTO interior,
        RudderDTO rudder,
        String color,
        BigDecimal price,
        boolean testDriveAvailable,
        boolean ordered
) { }
