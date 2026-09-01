package study.project.dealership.presentation.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import study.project.dealership.application.services.AssemblyOrderServiceImpl;
import study.project.dealership.contracts.assembly.model.AssemblyOrderDTO;
import study.project.dealership.contracts.assembly.request.RequestCreateAssemblyOrder;
import study.project.dealership.contracts.assembly.request.RequestUpdateAssemblyOrder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/assembly-orders")
@Validated
@RequiredArgsConstructor
public class AssemblyOrderController {

    private final AssemblyOrderServiceImpl assemblyOrderService;

    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    @PostMapping
    public ResponseEntity<AssemblyOrderDTO> create(@Valid @RequestBody RequestCreateAssemblyOrder request) {
        return ResponseEntity.ok(assemblyOrderService.create(request));
    }

    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<AssemblyOrderDTO>> findAll() {
        return ResponseEntity.ok(assemblyOrderService.findAll());
    }

    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AssemblyOrderDTO> findById(@PathVariable @NotNull UUID id) {
        return ResponseEntity.ok(assemblyOrderService.findById(id));
    }

    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    @PutMapping
    public ResponseEntity<AssemblyOrderDTO> update(@Valid @RequestBody RequestUpdateAssemblyOrder request) {
        return ResponseEntity.ok(assemblyOrderService.update(request));
    }

    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull UUID id) {
        assemblyOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
