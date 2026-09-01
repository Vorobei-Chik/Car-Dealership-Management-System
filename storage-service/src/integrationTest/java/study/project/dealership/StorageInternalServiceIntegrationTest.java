package study.project.dealership;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import study.project.dealership.application.services.StorageInternalService;
import study.project.dealership.domain.car.Car;
import study.project.dealership.common.messaging.OrderSentForApprovalEvent;
import study.project.dealership.common.messaging.OrderType;
import study.project.dealership.infrastructure.database.repository.CarJpaRepository;
import study.project.dealership.infrastructure.database.repository.EngineJpaRepository;
import study.project.dealership.support.IntegrationContainers;
import study.project.dealership.support.StorageCatalogFixture;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class StorageInternalServiceIntegrationTest extends IntegrationContainers {

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registerDataSource(registry);
        registerKafka(registry);
        registerGrpc(registry);
    }

    @Autowired
    StorageInternalService storageInternalService;

    @Autowired
    StorageCatalogFixture catalogFixture;

    @Autowired
    CarJpaRepository carJpaRepository;

    @Autowired
    EngineJpaRepository engineJpaRepository;

    @Test
    void fulfillPaidOrder_stockOrderWithReservedCar_returnsTrue() {
        var seed = catalogFixture.seedCatalogWithStockCar(true);
        UUID orderId = UUID.randomUUID();
        UUID traceId = UUID.randomUUID();

        boolean approved = storageInternalService.fulfillPaidOrder(new OrderSentForApprovalEvent(
                orderId, OrderType.STOCK, traceId, seed.carId(),
                null, null, null, null, null, null, null
        ));

        assertThat(approved).isTrue();
    }

    @Test
    void fulfillPaidOrder_stockOrderWithoutReservation_returnsFalse() {
        var seed = catalogFixture.seedCatalogWithStockCar(false);

        boolean approved = storageInternalService.fulfillPaidOrder(new OrderSentForApprovalEvent(
                UUID.randomUUID(), OrderType.STOCK, UUID.randomUUID(), seed.carId(),
                null, null, null, null, null, null, null
        ));

        assertThat(approved).isFalse();
    }

    @Test
    void fulfillPaidOrder_customOrderWithPartsInStock_decrementsInventory() {
        var seed = catalogFixture.seedCatalogWithStockCar(false);
        UUID traceId = UUID.randomUUID();

        boolean approved = storageInternalService.fulfillPaidOrder(new OrderSentForApprovalEvent(
                UUID.randomUUID(), OrderType.CUSTOM, traceId, null,
                seed.modelId(), seed.engineId(), seed.gearBoxId(), seed.transmissionId(),
                seed.wheelId(), seed.interiorId(), seed.rudderId()
        ));

        assertThat(approved).isTrue();
        assertThat(engineJpaRepository.findById(seed.engineId())).get()
                .extracting(e -> e.getQuantity().getValue()).isEqualTo(2);
    }

    @Test
    void releasePaidOrderReservation_stockOrder_releasesCar() {
        var seed = catalogFixture.seedCatalogWithStockCar(true);

        storageInternalService.releasePaidOrderReservation(new OrderSentForApprovalEvent(
                UUID.randomUUID(), OrderType.STOCK, UUID.randomUUID(), seed.carId(),
                null, null, null, null, null, null, null
        ));

        assertThat(carJpaRepository.findById(seed.carId())).get()
                .extracting(Car::isOrdered).isEqualTo(false);
    }
}
