package study.project.dealership.application.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.project.dealership.contracts.car.CarCatalogService;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.infrastructure.grpc.StorageGrpcCarClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarCatalogServiceImpl implements CarCatalogService {

    private final StorageGrpcCarClient storageGrpcCarClient;

    @Override
    public List<CarDTO> listAvailableCars() {
        return storageGrpcCarClient.listAvailableCars();
    }

    @Override
    public CarDTO getAvailableCar(UUID carId) {
        return storageGrpcCarClient.getAvailableCar(carId);
    }
}
