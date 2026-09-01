package study.project.dealership.infrastructure.messaging.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OutboxJpaRepository extends JpaRepository<OutboxMessage, UUID> {

    @Query("SELECT o FROM OutboxMessage o WHERE o.publishedAt IS NULL ORDER BY o.createdAt ASC")
    List<OutboxMessage> findUnpublished();
}
