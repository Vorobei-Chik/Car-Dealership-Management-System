package study.project.dealership.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;
import study.project.dealership.abstractions.repository.ConfigurationRepository;
import study.project.dealership.domain.car.Configuration;
import study.project.dealership.infrastructure.database.repository.ConfigurationJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ConfigurationRepositoryImpl implements ConfigurationRepository {

    private final ConfigurationJpaRepository configurationJpaRepository;

    @Override
    public void add(@NotNull Configuration config) {
        configurationJpaRepository.save(config);
    }

    @Override
    public void update(@NotNull Configuration config) {
        configurationJpaRepository.save(config);
    }

    @Override
    public void remove(UUID id) {
        configurationJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Configuration> find(UUID id) {
        return configurationJpaRepository.findById(id);
    }

    @Override
    public List<Configuration> getAll() {
        return configurationJpaRepository.findAllByStandardTrue();
    }

    @Override
    public Optional<Configuration> findByModelId(UUID id) {
        return configurationJpaRepository.findByCarModelIdAndStandardTrue(id);
    }
}