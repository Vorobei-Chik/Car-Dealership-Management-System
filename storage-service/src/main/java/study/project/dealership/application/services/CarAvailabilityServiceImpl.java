package study.project.dealership.application.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.project.dealership.application.exception.NotFoundException;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.contracts.mapping.CarMapper;
import study.project.dealership.infrastructure.database.repository.CarJpaRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarAvailabilityServiceImpl implements CarAvailabilityService {

    private final CarJpaRepository carJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CarDTO> listAvailableCars() {
        return carJpaRepository.findByRemovedFalseAndOrderedFalse().stream()
                .map(CarMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CarDTO getAvailableCar(UUID carId) {
        return carJpaRepository.findById(carId)
                .filter(car -> !car.isRemoved() && !car.isOrdered())
                .map(CarMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Car not available"));
    }
}
