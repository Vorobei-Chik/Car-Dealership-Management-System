package study.project.dealership.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.project.dealership.domain.part.Part;

import java.util.UUID;

@Repository
public interface PartJpaRepository extends JpaRepository<Part, UUID> {
}