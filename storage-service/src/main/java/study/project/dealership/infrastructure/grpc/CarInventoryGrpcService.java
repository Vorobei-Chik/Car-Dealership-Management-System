package study.project.dealership.infrastructure.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import study.project.dealership.application.exception.NotFoundException;
import study.project.dealership.application.services.CarAvailabilityService;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.grpc.inventory.*;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class CarInventoryGrpcService extends CarInventoryServiceGrpc.CarInventoryServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(CarInventoryGrpcService.class);

    private final CarAvailabilityService carAvailabilityService;

    @Override
    public void listAvailableCars(
            ListAvailableCarsRequest request,
            StreamObserver<ListAvailableCarsResponse> responseObserver
    ) {
        log.info("gRPC ListAvailableCars");
        try {
            List<CarDTO> cars = carAvailabilityService.listAvailableCars();
            ListAvailableCarsResponse response = ListAvailableCarsResponse.newBuilder()
                    .addAllCars(cars.stream()
                            .map(GrpcCarProtoMapper::toProto)
                            .toList())
                    .build();
            log.info("gRPC ListAvailableCars completed, count={}", response.getCarsCount());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            log.error("gRPC ListAvailableCars failed", ex);
            responseObserver.onError(Status.INTERNAL.withDescription(ex.getMessage()).withCause(ex).asRuntimeException());
        }
    }

    @Override
    public void getAvailableCar(
            GetAvailableCarRequest request,
            StreamObserver<GetAvailableCarResponse> responseObserver
    ) {
        log.info("gRPC GetAvailableCar carId={}", request.getCarId());
        try {
            CarDTO car = carAvailabilityService.getAvailableCar(GrpcCarProtoMapper.parseId(request.getCarId()));
            GetAvailableCarResponse response = GetAvailableCarResponse.newBuilder()
                    .setCar(GrpcCarProtoMapper.toProto(car))
                    .build();
            log.info("gRPC GetAvailableCar completed carId={}", request.getCarId());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (NotFoundException ex) {
            log.warn("gRPC GetAvailableCar not found carId={}", request.getCarId());
            responseObserver.onError(Status.NOT_FOUND.withDescription(ex.getMessage()).asRuntimeException());
        } catch (IllegalArgumentException ex) {
            log.warn("gRPC GetAvailableCar invalid id carId={}", request.getCarId());
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asRuntimeException());
        } catch (Exception ex) {
            log.error("gRPC GetAvailableCar failed carId={}", request.getCarId(), ex);
            responseObserver.onError(Status.INTERNAL.withDescription(ex.getMessage()).withCause(ex).asRuntimeException());
        }
    }
}
