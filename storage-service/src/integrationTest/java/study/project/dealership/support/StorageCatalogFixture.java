package study.project.dealership.support;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.project.dealership.domain.car.Car;
import study.project.dealership.domain.car.Model;
import study.project.dealership.domain.part.*;
import study.project.dealership.domain.valueobject.Money;
import study.project.dealership.domain.valueobject.Quantity;
import study.project.dealership.domain.valueobject.carinfo.BodyType;
import study.project.dealership.domain.valueobject.carinfo.Color;
import study.project.dealership.domain.valueobject.carinfo.VehicleDriveType;
import study.project.dealership.domain.valueobject.engineinfo.EngineCapacity;
import study.project.dealership.domain.valueobject.engineinfo.EnginePower;
import study.project.dealership.domain.valueobject.engineinfo.FuelType;
import study.project.dealership.domain.valueobject.gearboxinfo.GearBoxType;
import study.project.dealership.infrastructure.database.repository.*;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class StorageCatalogFixture {

    private final ModelJpaRepository modelRepository;
    private final EngineJpaRepository engineRepository;
    private final GearBoxJpaRepository gearBoxRepository;
    private final TransmissionJpaRepository transmissionRepository;
    private final WheelJpaRepository wheelRepository;
    private final InteriorJpaRepository interiorRepository;
    private final RudderJpaRepository rudderRepository;
    private final PartCompatibilityJpaRepository partCompatibilityRepository;
    private final CarJpaRepository carRepository;

    public StorageCatalogFixture(
            ModelJpaRepository modelRepository,
            EngineJpaRepository engineRepository,
            GearBoxJpaRepository gearBoxRepository,
            TransmissionJpaRepository transmissionRepository,
            WheelJpaRepository wheelRepository,
            InteriorJpaRepository interiorRepository,
            RudderJpaRepository rudderRepository,
            PartCompatibilityJpaRepository partCompatibilityRepository,
            CarJpaRepository carRepository
    ) {
        this.modelRepository = modelRepository;
        this.engineRepository = engineRepository;
        this.gearBoxRepository = gearBoxRepository;
        this.transmissionRepository = transmissionRepository;
        this.wheelRepository = wheelRepository;
        this.interiorRepository = interiorRepository;
        this.rudderRepository = rudderRepository;
        this.partCompatibilityRepository = partCompatibilityRepository;
        this.carRepository = carRepository;
    }

    @Transactional
    public CatalogSeed seedCatalogWithStockCar(boolean carOrdered) {
        Model model = modelRepository.save(Model.create(
                "TestBrand",
                BodyType.SEDAN,
                new Money(BigDecimal.valueOf(1_000_000))
        ));

        Engine engine = saveEngine(Engine.create(
                FuelType.GAS,
                new EnginePower(BigDecimal.valueOf(150)),
                new EngineCapacity(BigDecimal.valueOf(2.0)),
                new Money(BigDecimal.valueOf(200_000))
        ), 3, model.getId());

        GearBox gearBox = saveGearBox(
                GearBox.create(GearBoxType.AUTOMATIC, new Money(BigDecimal.valueOf(80_000))), 3, model.getId());
        Transmission transmission = saveTransmission(
                Transmission.create(VehicleDriveType.FULL, new Money(BigDecimal.valueOf(60_000))), 3, model.getId());
        Wheel wheel = saveWheel(Wheel.create(new Money(BigDecimal.valueOf(40_000))), 3, model.getId());
        Interior interior = saveInterior(Interior.create(new Money(BigDecimal.valueOf(50_000))), 3, model.getId());
        Rudder rudder = saveRudder(Rudder.create(new Money(BigDecimal.valueOf(10_000))), 3, model.getId());

        Car car = Car.create(model, engine, gearBox, transmission, wheel, interior, rudder, new Color("#FFFFFF"));
        car.setOrdered(carOrdered);
        car = carRepository.save(car);

        return new CatalogSeed(model.getId(), engine.getId(), gearBox.getId(), transmission.getId(),
                wheel.getId(), interior.getId(), rudder.getId(), car.getId());
    }

    private Engine saveEngine(Engine part, int quantity, UUID modelId) {
        part.setQuantity(new Quantity(quantity));
        Engine saved = engineRepository.save(part);
        linkCompatibility(modelId, saved.getId());
        return saved;
    }

    private GearBox saveGearBox(GearBox part, int quantity, UUID modelId) {
        part.setQuantity(new Quantity(quantity));
        GearBox saved = gearBoxRepository.save(part);
        linkCompatibility(modelId, saved.getId());
        return saved;
    }

    private Transmission saveTransmission(Transmission part, int quantity, UUID modelId) {
        part.setQuantity(new Quantity(quantity));
        Transmission saved = transmissionRepository.save(part);
        linkCompatibility(modelId, saved.getId());
        return saved;
    }

    private Wheel saveWheel(Wheel part, int quantity, UUID modelId) {
        part.setQuantity(new Quantity(quantity));
        Wheel saved = wheelRepository.save(part);
        linkCompatibility(modelId, saved.getId());
        return saved;
    }

    private Interior saveInterior(Interior part, int quantity, UUID modelId) {
        part.setQuantity(new Quantity(quantity));
        Interior saved = interiorRepository.save(part);
        linkCompatibility(modelId, saved.getId());
        return saved;
    }

    private Rudder saveRudder(Rudder part, int quantity, UUID modelId) {
        part.setQuantity(new Quantity(quantity));
        Rudder saved = rudderRepository.save(part);
        linkCompatibility(modelId, saved.getId());
        return saved;
    }

    private void linkCompatibility(UUID modelId, UUID partId) {
        PartCompatibility compatibility = new PartCompatibility();
        compatibility.setModelId(modelId);
        compatibility.setPartId(partId);
        partCompatibilityRepository.save(compatibility);
    }

    public record CatalogSeed(
            UUID modelId,
            UUID engineId,
            UUID gearBoxId,
            UUID transmissionId,
            UUID wheelId,
            UUID interiorId,
            UUID rudderId,
            UUID carId
    ) {
    }
}
