package study.project.dealership.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.project.dealership.domain.order.StockOrder;

import java.util.List;
import java.util.UUID;

@Repository
public interface StockOrderJpaRepository extends JpaRepository<StockOrder, UUID> {
    List<StockOrder> findAllByClientId(UUID clientId);
}