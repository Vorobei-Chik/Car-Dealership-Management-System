package study.project.dealership.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.project.dealership.domain.part.GearBox;

import java.util.UUID;

@Repository
public interface GearBoxJpaRepository extends JpaRepository<GearBox, UUID> {
}