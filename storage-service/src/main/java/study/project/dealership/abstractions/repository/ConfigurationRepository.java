package study.project.dealership.abstractions.repository;

import study.project.dealership.domain.car.Configuration;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConfigurationRepository {
    void add(Configuration config);
    void update(Configuration config);
    void remove(UUID id);

    Optional<Configuration> find(UUID id);
    List<Configuration> getAll();

    Optional<Configuration> findByModelId(UUID id);
}
