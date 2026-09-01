package study.project.dealership.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.project.dealership.domain.assembly.AssemblyOrder;

import java.util.UUID;

public interface AssemblyOrderJpaRepository extends JpaRepository<AssemblyOrder, UUID> {
}
