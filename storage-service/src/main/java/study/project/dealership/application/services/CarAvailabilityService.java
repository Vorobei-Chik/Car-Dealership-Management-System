package study.project.dealership.application.services;

import study.project.dealership.contracts.car.model.CarDTO;

import java.util.List;
import java.util.UUID;

public interface CarAvailabilityService {

    List<CarDTO> listAvailableCars();

    CarDTO getAvailableCar(UUID carId);
}
