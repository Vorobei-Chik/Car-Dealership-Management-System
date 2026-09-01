package study.project.dealership.presentation.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import study.project.dealership.contracts.car.CarService;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.contracts.car.model.ConfigurationDTO;
import study.project.dealership.contracts.car.model.ModelDTO;
import study.project.dealership.contracts.car.request.*;
import study.project.dealership.contracts.mapping.CarMapper;
import study.project.dealership.contracts.mapping.ConfigurationMapper;
import study.project.dealership.contracts.mapping.ModelMapper;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cars")
@Validated
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CarDTO> createCar(@Valid @RequestBody RequestCreateCar request) {
        return ResponseEntity.ok(CarMapper.toDto(carService.createCar(request)));
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PostMapping("/standard")
    public ResponseEntity<List<CarDTO>> createStandardCar(@Valid @RequestBody RequestCreateStandardCar request) {
        List<CarDTO> dtos = carService.createStandardCar(request).stream()
                .map(CarMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<CarDTO>> getAllCars() {
        List<CarDTO> dtos = carService.getAllCars().stream()
                .map(CarMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/filter")
    public ResponseEntity<List<CarDTO>> findFilteredCars(@Valid @ModelAttribute RequestFindFilteredCars request) {
        List<CarDTO> dtos = carService.findFilteredCars(request).stream()
                .map(CarMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> findCar(@PathVariable @NotNull UUID id) {
        return ResponseEntity.ok(CarMapper.toDto(carService.findCar(new RequestFindCar(id))));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{carId}")
    public ResponseEntity<Void> removeCar(@PathVariable @NotNull UUID carId) {
        carService.removeCar(new RequestRemoveCar(carId));
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<CarDTO> updateCar(@Valid @RequestBody RequestUpdateCar request) {
        return ResponseEntity.ok(CarMapper.toDto(carService.updateCar(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/configurations/standard")
    public ResponseEntity<ConfigurationDTO> createStandardConfiguration(@Valid @RequestBody RequestCreateStandardConfiguration request) {
        return ResponseEntity.ok(ConfigurationMapper.toDto(carService.createStandardConfiguration(request)));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/configurations")
    public ResponseEntity<List<ConfigurationDTO>> getAllStandardConfigurations() {
        List<ConfigurationDTO> dtos = carService.getAllStandardConfigurations().stream()
                .map(ConfigurationMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/configurations/by-model")
    public ResponseEntity<ConfigurationDTO> findStandardConfigurationByModel(@RequestParam @NotNull UUID modelId) {
        return ResponseEntity.ok(ConfigurationMapper.toDto(
                carService.findStandardConfigurationByModel(new RequestFindStandardConfigurationByModel(modelId))));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/configurations/{id}")
    public ResponseEntity<ConfigurationDTO> findStandardConfiguration(@PathVariable @NotNull UUID id) {
        return ResponseEntity.ok(ConfigurationMapper.toDto(
                carService.findStandardConfiguration(new RequestFindStandardConfiguration(id))));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/configurations/{id}")
    public ResponseEntity<Void> removeStandardConfiguration(@PathVariable @NotNull UUID id) {
        carService.removeStandardConfiguration(new RequestRemoveStandardConfiguration(id));
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/configurations")
    public ResponseEntity<ConfigurationDTO> updateStandardConfiguration(@Valid @RequestBody RequestUpdateStandardConfiguration request) {
        return ResponseEntity.ok(ConfigurationMapper.toDto(carService.updateStandardConfiguration(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/models")
    public ResponseEntity<ModelDTO> createModel(@Valid @RequestBody RequestCreateModel request) {
        return ResponseEntity.ok(ModelMapper.toDto(carService.createModel(request)));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/models")
    public ResponseEntity<List<ModelDTO>> getAllModels() {
        List<ModelDTO> dtos = carService.getAllModels().stream()
                .map(ModelMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/models/{id}")
    public ResponseEntity<ModelDTO> findModel(@PathVariable @NotNull UUID id) {
        return ResponseEntity.ok(ModelMapper.toDto(carService.findModel(new RequestFindModel(id))));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/models/{id}")
    public ResponseEntity<Void> removeModel(@PathVariable @NotNull UUID id) {
        carService.removeModel(new RequestRemoveModel(id));
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/models")
    public ResponseEntity<ModelDTO> updateModel(@Valid @RequestBody RequestUpdateModel request) {
        return ResponseEntity.ok(ModelMapper.toDto(carService.updateModel(request)));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/test-drive/add")
    public ResponseEntity<CarDTO> addCarToTestDrive(@Valid @RequestBody RequestAddCarToTestDrive request) {
        return ResponseEntity.ok(CarMapper.toDto(carService.addCarToTestDrive(request)));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/test-drive/remove")
    public ResponseEntity<Void> removeCarFromTestDrive(@Valid @RequestBody RequestRemoveCarFromTestDrive request) {
        carService.removeCarFromTestDrive(request);
        return ResponseEntity.ok().build();
    }
}
