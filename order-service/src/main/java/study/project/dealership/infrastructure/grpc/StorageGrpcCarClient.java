package study.project.dealership.infrastructure.grpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import study.project.dealership.application.exception.NotFoundException;
import study.project.dealership.application.exception.ServiceUnavailableException;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.grpc.inventory.CarInventoryServiceGrpc;
import study.project.dealership.grpc.inventory.GetAvailableCarRequest;
import study.project.dealership.grpc.inventory.ListAvailableCarsRequest;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class StorageGrpcCarClient {

    private static final Logger log = LoggerFactory.getLogger(StorageGrpcCarClient.class);

    private final CarInventoryServiceGrpc.CarInventoryServiceBlockingStub carInventoryStub;
    private final long deadlineMs;

    public StorageGrpcCarClient(
            @GrpcClient("storage-service") CarInventoryServiceGrpc.CarInventoryServiceBlockingStub carInventoryStub,
            @Value("${app.storage-service.grpc.deadline-ms:5000}") long deadlineMs) {
        this.carInventoryStub = carInventoryStub;
        this.deadlineMs = deadlineMs;
    }

    public List<CarDTO> listAvailableCars() {
        log.info("Calling storage gRPC ListAvailableCars");
        try {
            List<CarDTO> cars = GrpcCarProtoMapper.toDtoList(
                    carInventoryStub
                            .withDeadlineAfter(deadlineMillis(), TimeUnit.MILLISECONDS)
                            .listAvailableCars(ListAvailableCarsRequest.getDefaultInstance())
                            .getCarsList()
            );
            log.info("storage gRPC ListAvailableCars returned {} cars", cars.size());
            return cars;
        } catch (StatusRuntimeException ex) {
            throw translateStatus(ex, "list available cars");
        } catch (Exception ex) {
            log.error("storage gRPC ListAvailableCars failed", ex);
            throw new ServiceUnavailableException("Storage service is unavailable", ex);
        }
    }

    public CarDTO getAvailableCar(UUID carId) {
        log.info("Calling storage gRPC GetAvailableCar carId={}", carId);
        try {
            CarDTO car = GrpcCarProtoMapper.toDto(
                    carInventoryStub
                            .withDeadlineAfter(deadlineMillis(), TimeUnit.MILLISECONDS)
                            .getAvailableCar(GetAvailableCarRequest.newBuilder()
                                    .setCarId(carId.toString())
                                    .build())
                            .getCar()
            );
            log.info("storage gRPC GetAvailableCar completed carId={}", carId);
            return car;
        } catch (StatusRuntimeException ex) {
            throw translateStatus(ex, "get available car");
        } catch (Exception ex) {
            log.error("storage gRPC GetAvailableCar failed carId={}", carId, ex);
            throw new ServiceUnavailableException("Storage service is unavailable", ex);
        }
    }

    private RuntimeException translateStatus(StatusRuntimeException ex, String operation) {
        Status.Code code = ex.getStatus().getCode();
        log.warn("storage gRPC {} failed: {} {}", operation, code, ex.getStatus().getDescription());
        return switch (code) {
            case NOT_FOUND -> new NotFoundException("Car not available");
            case INVALID_ARGUMENT -> new NotFoundException("Car not found");
            case UNAVAILABLE, DEADLINE_EXCEEDED, CANCELLED ->
                    new ServiceUnavailableException("Storage service is unavailable", ex);
            default -> new ServiceUnavailableException("Storage service is unavailable", ex);
        };
    }

    private long deadlineMillis() {
        return deadlineMs;
    }
}
