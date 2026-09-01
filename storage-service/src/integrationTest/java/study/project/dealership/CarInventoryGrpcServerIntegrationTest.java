package study.project.dealership;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import study.project.dealership.infrastructure.database.repository.CarJpaRepository;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import study.project.dealership.grpc.inventory.CarInventoryServiceGrpc;
import study.project.dealership.grpc.inventory.GetAvailableCarRequest;
import study.project.dealership.grpc.inventory.ListAvailableCarsRequest;
import study.project.dealership.support.IntegrationContainers;
import study.project.dealership.support.StorageCatalogFixture;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
class CarInventoryGrpcServerIntegrationTest extends IntegrationContainers {

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registerDataSource(registry);
        registerKafka(registry);
        registerGrpc(registry);
    }

    @GrpcClient("storage-grpc-it")
    private CarInventoryServiceGrpc.CarInventoryServiceBlockingStub carInventoryStub;

    @Autowired
    private StorageCatalogFixture catalogFixture;

    @Autowired
    private CarJpaRepository carJpaRepository;

    @BeforeEach
    void cleanCars() {
        carJpaRepository.deleteAll();
    }

    @Test
    void listAvailableCars_returnsOnlyUnorderedCars() {
        var available = catalogFixture.seedCatalogWithStockCar(false);
        catalogFixture.seedCatalogWithStockCar(true);

        var response = carInventoryStub.listAvailableCars(ListAvailableCarsRequest.getDefaultInstance());

        assertThat(response.getCarsList()).hasSize(1);
        assertThat(response.getCars(0).getId()).isEqualTo(available.carId().toString());
        assertThat(response.getCars(0).getOrdered()).isFalse();
    }

    @Test
    void listAvailableCars_returnsEmptyWhenNoStock() {
        var response = carInventoryStub.listAvailableCars(ListAvailableCarsRequest.getDefaultInstance());
        assertThat(response.getCarsList()).isEmpty();
    }

    @Test
    void getAvailableCar_returnsCarWhenInStock() {
        var seed = catalogFixture.seedCatalogWithStockCar(false);

        var response = carInventoryStub.getAvailableCar(
                GetAvailableCarRequest.newBuilder().setCarId(seed.carId().toString()).build()
        );

        assertThat(response.getCar().getId()).isEqualTo(seed.carId().toString());
    }

    @Test
    void getAvailableCar_notFoundWhenOrdered() {
        var seed = catalogFixture.seedCatalogWithStockCar(true);

        assertThatThrownBy(() -> carInventoryStub.getAvailableCar(
                GetAvailableCarRequest.newBuilder().setCarId(seed.carId().toString()).build()
        )).hasMessageContaining("NOT_FOUND");
    }

    @Test
    void getAvailableCar_notFoundForUnknownId() {
        assertThatThrownBy(() -> carInventoryStub.getAvailableCar(
                GetAvailableCarRequest.newBuilder().setCarId(UUID.randomUUID().toString()).build()
        )).hasMessageContaining("NOT_FOUND");
    }
}
