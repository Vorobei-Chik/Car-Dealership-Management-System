package study.project.dealership.abstractions.repository;

import study.project.dealership.domain.request.RequestTestDrive;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface RequestRepository {
    void add(RequestTestDrive request);
    void update(RequestTestDrive request);
    void remove(UUID id);
    Optional<RequestTestDrive> find(UUID id);
    List<RequestTestDrive> getAll();
    List<RequestTestDrive> getByUserId(UUID id);
}
