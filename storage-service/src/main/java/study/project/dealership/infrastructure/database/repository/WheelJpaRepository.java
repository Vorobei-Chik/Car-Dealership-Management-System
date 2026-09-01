package study.project.dealership.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.project.dealership.domain.part.Wheel;

import java.util.UUID;

@Repository
public interface WheelJpaRepository extends JpaRepository<Wheel, UUID> {
}