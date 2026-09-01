package study.project.dealership.contracts.car;

import study.project.dealership.contracts.car.model.CarDTO;

import java.util.List;
import java.util.UUID;

public interface CarCatalogService {

    List<CarDTO> listAvailableCars();

    CarDTO getAvailableCar(UUID carId);
}
