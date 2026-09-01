package study.project.dealership;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import study.project.dealership.application.exception.NotFoundException;
import study.project.dealership.application.exception.ServiceUnavailableException;
import study.project.dealership.contracts.car.CarCatalogService;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.contracts.car.model.ModelDTO;
import study.project.dealership.contracts.car.model.type.BodyTypeDTO;
import study.project.dealership.contracts.part.model.*;
import study.project.dealership.contracts.part.model.type.FuelTypeDTO;
import study.project.dealership.contracts.part.model.type.GearBoxTypeDTO;
import study.project.dealership.contracts.part.model.type.VehicleDriveTypeDTO;
import study.project.dealership.infrastructure.grpc.StorageGrpcCarClient;
import study.project.dealership.support.OrderIntegrationContainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Testcontainers
@SpringBootTest
class CarCatalogGrpcClientIntegrationTest extends OrderIntegrationContainers {

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registerDataSource(registry);
        registerKafka(registry);
    }

    @MockitoBean
    private StorageGrpcCarClient storageGrpcCarClient;

    @Autowired
    private CarCatalogService carCatalogService;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(storageGrpcCarClient);
    }

    @Test
    void listAvailableCars_returnsCarsFromGrpcClient() {
        CarDTO car = sampleCar();
        when(storageGrpcCarClient.listAvailableCars()).thenReturn(List.of(car));

        assertThat(carCatalogService.listAvailableCars()).containsExactly(car);
    }

    @Test
    void listAvailableCars_emptyResult() {
        when(storageGrpcCarClient.listAvailableCars()).thenReturn(List.of());

        assertThat(carCatalogService.listAvailableCars()).isEmpty();
    }

    @Test
    void getAvailableCar_returnsCarFromGrpcClient() {
        CarDTO car = sampleCar();
        when(storageGrpcCarClient.getAvailableCar(car.id())).thenReturn(car);

        assertThat(carCatalogService.getAvailableCar(car.id())).isEqualTo(car);
    }

    @Test
    void listAvailableCars_serviceUnavailable() {
        when(storageGrpcCarClient.listAvailableCars())
                .thenThrow(new ServiceUnavailableException("Storage service is unavailable",
                        new StatusRuntimeException(Status.UNAVAILABLE)));

        assertThatThrownBy(() -> carCatalogService.listAvailableCars())
                .isInstanceOf(ServiceUnavailableException.class);
    }

    @Test
    void getAvailableCar_notFound() {
        UUID carId = UUID.randomUUID();
        when(storageGrpcCarClient.getAvailableCar(carId))
                .thenThrow(new NotFoundException("Car not available"));

        assertThatThrownBy(() -> carCatalogService.getAvailableCar(carId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getAvailableCar_timeoutMappedToServiceUnavailable() {
        UUID carId = UUID.randomUUID();
        when(storageGrpcCarClient.getAvailableCar(any()))
                .thenThrow(new ServiceUnavailableException("Storage service is unavailable",
                        new StatusRuntimeException(Status.DEADLINE_EXCEEDED)));

        assertThatThrownBy(() -> carCatalogService.getAvailableCar(carId))
                .isInstanceOf(ServiceUnavailableException.class);
    }

    private static CarDTO sampleCar() {
        UUID partId = UUID.randomUUID();
        return new CarDTO(
                UUID.randomUUID(),
                new ModelDTO(UUID.randomUUID(), "Brand", BodyTypeDTO.SEDAN, BigDecimal.valueOf(1_000_000)),
                new EngineDTO(partId, FuelTypeDTO.GAS, BigDecimal.TEN, BigDecimal.ONE, BigDecimal.valueOf(100), 1),
                new GearBoxDTO(partId, GearBoxTypeDTO.AUTOMATIC, BigDecimal.valueOf(80), 1),
                new TransmissionDTO(partId, VehicleDriveTypeDTO.FULL, BigDecimal.valueOf(60), 1),
                new WheelDTO(partId, BigDecimal.valueOf(40), 1),
                new InteriorDTO(partId, BigDecimal.valueOf(50), 1),
                new RudderDTO(partId, BigDecimal.TEN, 1),
                "#FFFFFF",
                BigDecimal.valueOf(1_340_000),
                false,
                false
        );
    }
}
