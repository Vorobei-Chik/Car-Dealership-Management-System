package study.project.dealership.application.services;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.project.dealership.abstractions.repository.RequestRepository;
import study.project.dealership.application.exception.BadRequestException;
import study.project.dealership.application.exception.ConflictException;
import study.project.dealership.application.exception.NotFoundException;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.contracts.request.RequestService;
import study.project.dealership.contracts.request.request.*;
import study.project.dealership.domain.request.RequestTestDrive;
import study.project.dealership.infrastructure.client.StorageClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final StorageClient storageClient;

    @Override
    @Transactional
    public RequestTestDrive createRequest(@NotNull RequestCreateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID clientId = UUID.fromString(auth.getName());

        CarDTO car = storageClient.getCar(request.carId());
        if (car.ordered()) {
            throw new ConflictException("Cannot request test drive for ordered car");
        }
        if (!car.testDriveAvailable()) {
            throw new ConflictException("Car is not available for test drive");
        }

        RequestTestDrive domainRequest = RequestTestDrive.create(clientId, request.carId(), request.date());
        requestRepository.add(domainRequest);
        return domainRequest;
    }

    @Override
    @Transactional(readOnly = true)
    public RequestTestDrive findRequest(@NotNull RequestFindRequest request) {
        return requestRepository.find(request.id())
                .orElseThrow(() -> new NotFoundException("Request not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestTestDrive> getAllRequests() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
            return requestRepository.getAll();
        }
        return requestRepository.getByUserId(UUID.fromString(auth.getName()));
    }

    @Override
    @Transactional
    public void removeRequest(@NotNull RequestRemoveRequest request) {
        if (requestRepository.find(request.id()).isEmpty()) {
            throw new NotFoundException("Request not found");
        }
        requestRepository.remove(request.id());
    }

    @Override
    @Transactional
    public RequestTestDrive updateRequest(@NotNull RequestUpdateRequest request) {
        RequestTestDrive existing = requestRepository.find(request.id())
                .orElseThrow(() -> new NotFoundException("Request not found"));

        CarDTO car = storageClient.getCar(request.carId());
        if (car.ordered()) {
            throw new ConflictException("Cannot update request to an ordered car");
        }
        if (!car.testDriveAvailable()) {
            throw new ConflictException("Car is not available for test drive");
        }

        LocalDateTime date = request.date();
        if (date.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Test drive date cannot be in the past");
        }

        existing.setClientId(request.clientId());
        existing.setCarId(request.carId());
        existing.setDate(date);
        requestRepository.update(existing);
        return existing;
    }
}
