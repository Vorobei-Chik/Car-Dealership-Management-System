package study.project.dealership.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.project.dealership.contracts.mapping.RequestMapper;
import study.project.dealership.contracts.request.model.RequestDTO;
import study.project.dealership.domain.request.RequestTestDrive;
import study.project.dealership.infrastructure.client.StorageClient;

@Component
@RequiredArgsConstructor
public class RequestDtoFactory {

    private final StorageClient storageClient;

    public RequestDTO toDto(RequestTestDrive request) {
        return RequestMapper.toDto(request, storageClient.getCar(request.getCarId()));
    }
}
