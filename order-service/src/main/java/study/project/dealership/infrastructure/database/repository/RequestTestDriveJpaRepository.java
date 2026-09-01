package study.project.dealership.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.project.dealership.domain.request.RequestTestDrive;

import java.util.List;
import java.util.UUID;

@Repository
public interface RequestTestDriveJpaRepository extends JpaRepository<RequestTestDrive, UUID> {
    List<RequestTestDrive> findAllByClientId(UUID clientId);
}