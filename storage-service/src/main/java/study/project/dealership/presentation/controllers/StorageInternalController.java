package study.project.dealership.presentation.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import study.project.dealership.application.services.StorageInternalService;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.contracts.car.model.ConfigurationDTO;
import study.project.dealership.contracts.order.request.RequestCreateCustomOrder;

import java.util.UUID;

@RestController
@RequestMapping("/api/internal")
@RequiredArgsConstructor
public class StorageInternalController {

    private final StorageInternalService storageInternalService;

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @PostMapping("/cars/{id}/reserve")
    public ResponseEntity<Void> reserveCar(@PathVariable UUID id) {
        storageInternalService.reserveCar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PostMapping("/cars/{id}/release")
    public ResponseEntity<Void> releaseCar(@PathVariable UUID id) {
        storageInternalService.releaseCar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @PostMapping("/orders/custom/reserve")
    public ResponseEntity<Void> reserveCustomOrder(@Valid @RequestBody RequestCreateCustomOrder request) {
        storageInternalService.validateAndReserveCustomOrder(request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PostMapping("/orders/custom/release")
    public ResponseEntity<Void> releaseCustomOrder(
            @RequestParam UUID modelId,
            @RequestParam UUID engineId,
            @RequestParam UUID gearBoxId,
            @RequestParam UUID transmissionId,
            @RequestParam UUID wheelId,
            @RequestParam UUID interiorId,
            @RequestParam UUID rudderId
    ) {
        storageInternalService.releaseCustomOrderParts(modelId, engineId, gearBoxId, transmissionId, wheelId, interiorId, rudderId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @GetMapping("/configurations/snapshot")
    public ResponseEntity<ConfigurationDTO> configurationSnapshot(
            @RequestParam UUID modelId,
            @RequestParam UUID engineId,
            @RequestParam UUID gearBoxId,
            @RequestParam UUID transmissionId,
            @RequestParam UUID wheelId,
            @RequestParam UUID interiorId,
            @RequestParam UUID rudderId,
            @RequestParam String color
    ) {
        return ResponseEntity.ok(storageInternalService.configurationSnapshot(
                modelId, engineId, gearBoxId, transmissionId, wheelId, interiorId, rudderId, color
        ));
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @PostMapping("/cars/build")
    public ResponseEntity<CarDTO> buildCar(@Valid @RequestBody ConfigurationDTO configuration) {
        return ResponseEntity.ok(storageInternalService.buildCar(configuration));
    }
}
