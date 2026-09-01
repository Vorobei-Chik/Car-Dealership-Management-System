package study.project.dealership.presentation.controllers;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.project.dealership.contracts.car.CarCatalogService;
import study.project.dealership.contracts.car.model.CarDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cars")
@Validated
@RequiredArgsConstructor
public class CarCatalogController {

    private final CarCatalogService carCatalogService;

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<CarDTO>> listAvailableCars() {
        return ResponseEntity.ok(carCatalogService.listAvailableCars());
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getAvailableCar(@PathVariable @NotNull UUID id) {
        return ResponseEntity.ok(carCatalogService.getAvailableCar(id));
    }
}
