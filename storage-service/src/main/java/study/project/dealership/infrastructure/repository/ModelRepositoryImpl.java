package study.project.dealership.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.project.dealership.abstractions.repository.ModelRepository;
import study.project.dealership.domain.car.Model;
import study.project.dealership.infrastructure.database.repository.ModelJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ModelRepositoryImpl implements ModelRepository {

    private final ModelJpaRepository modelJpaRepository;

    @Override
    public void add(Model model) {
        modelJpaRepository.save(model);
    }

    @Override
    public void update(Model model) {
        modelJpaRepository.save(model);
    }

    @Override
    public void remove(UUID modelId) {
        modelJpaRepository.deleteById(modelId);
    }

    @Override
    public Optional<Model> find(UUID modelId) {
        return modelJpaRepository.findById(modelId);
    }

    @Override
    public List<Model> getAll() {
        return modelJpaRepository.findAll();
    }
}