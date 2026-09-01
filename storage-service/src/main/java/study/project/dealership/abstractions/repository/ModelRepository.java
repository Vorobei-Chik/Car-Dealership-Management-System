package study.project.dealership.abstractions.repository;

import study.project.dealership.domain.car.Model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModelRepository {
    void add(Model model);
    void update(Model model);
    void remove(UUID modelId);

    Optional<Model> find(UUID modelId);
    List<Model> getAll();
}
