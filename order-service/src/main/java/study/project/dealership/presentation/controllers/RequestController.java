package study.project.dealership.presentation.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import study.project.dealership.application.RequestDtoFactory;
import study.project.dealership.contracts.request.RequestService;
import study.project.dealership.contracts.request.model.RequestDTO;
import study.project.dealership.contracts.request.request.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
@Validated
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;
    private final RequestDtoFactory requestDtoFactory;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<RequestDTO> createRequest(@Valid @RequestBody RequestCreateRequest request) {
        return ResponseEntity.ok(requestDtoFactory.toDto(requestService.createRequest(request)));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<RequestDTO>> getAllRequests() {
        return ResponseEntity.ok(requestService.getAllRequests().stream().map(requestDtoFactory::toDto).toList());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<RequestDTO> findRequest(@PathVariable @NotNull UUID id) {
        return ResponseEntity.ok(requestDtoFactory.toDto(requestService.findRequest(new RequestFindRequest(id))));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeRequest(@PathVariable @NotNull UUID id) {
        requestService.removeRequest(new RequestRemoveRequest(id));
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<RequestDTO> updateRequest(@Valid @RequestBody RequestUpdateRequest request) {
        return ResponseEntity.ok(requestDtoFactory.toDto(requestService.updateRequest(request)));
    }
}
