package study.project.dealership.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.project.dealership.abstractions.repository.RequestRepository;
import study.project.dealership.domain.request.RequestTestDrive;
import study.project.dealership.infrastructure.database.repository.RequestTestDriveJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RequestRepositoryImpl implements RequestRepository {

    private final RequestTestDriveJpaRepository requestTestDriveJpaRepository;

    @Override
    public void add(RequestTestDrive request) {
        requestTestDriveJpaRepository.save(request);
    }

    @Override
    public void update(RequestTestDrive request) {
        requestTestDriveJpaRepository.save(request);
    }

    @Override
    public void remove(UUID id) {
        requestTestDriveJpaRepository.deleteById(id);
    }

    @Override
    public Optional<RequestTestDrive> find(UUID id) {
        return requestTestDriveJpaRepository.findById(id);
    }

    @Override
    public List<RequestTestDrive> getAll() {
        return requestTestDriveJpaRepository.findAll();
    }

    @Override
    public List<RequestTestDrive> getByUserId(UUID id) {
        return requestTestDriveJpaRepository.findAllByClientId(id);
    }
}