package study.project.dealership.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.project.dealership.domain.car.Configuration;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfigurationJpaRepository extends JpaRepository<Configuration, UUID> {

    List<Configuration> findAllByStandardTrue();

    Optional<Configuration> findByCarModelIdAndStandardTrue(UUID id);
}