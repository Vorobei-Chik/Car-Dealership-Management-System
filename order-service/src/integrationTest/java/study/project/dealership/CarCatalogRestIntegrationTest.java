package study.project.dealership;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import study.project.dealership.application.exception.NotFoundException;
import study.project.dealership.application.exception.ServiceUnavailableException;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class CarCatalogRestIntegrationTest extends OrderIntegrationContainers {

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registerDataSource(registry);
        registerKafka(registry);
    }

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StorageGrpcCarClient storageGrpcCarClient;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(storageGrpcCarClient);
    }

    @Test
    @WithMockUser(roles = "USER")
    void listCars_returnsOk() throws Exception {
        CarDTO car = sampleCar();
        when(storageGrpcCarClient.listAvailableCars()).thenReturn(List.of(car));

        mockMvc.perform(get("/api/cars").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(car.id().toString()))
                .andExpect(jsonPath("$[0].ordered").value(false));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void getCarById_returnsOk() throws Exception {
        CarDTO car = sampleCar();
        when(storageGrpcCarClient.getAvailableCar(car.id())).thenReturn(car);

        mockMvc.perform(get("/api/cars/{id}", car.id()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(car.id().toString()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getCarById_notFound() throws Exception {
        UUID carId = UUID.randomUUID();
        when(storageGrpcCarClient.getAvailableCar(carId)).thenThrow(new NotFoundException("Car not available"));

        mockMvc.perform(get("/api/cars/{id}", carId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void listCars_serviceUnavailable() throws Exception {
        when(storageGrpcCarClient.listAvailableCars())
                .thenThrow(new ServiceUnavailableException("Storage service is unavailable"));

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isServiceUnavailable());
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
