package study.project.dealership.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import study.project.dealership.domain.part.PartCompatibility;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartCompatibilityJpaRepository extends JpaRepository<PartCompatibility, UUID> {
    Optional<PartCompatibility> findByModelIdAndPartId(UUID modelId, UUID partId);

    boolean existsByModelIdAndPartId(UUID modelId, UUID partId);

    @Query("SELECT pc.partId FROM PartCompatibility pc WHERE pc.modelId = :modelId")
    List<UUID> findPartIdsByModelId(UUID modelId);
}