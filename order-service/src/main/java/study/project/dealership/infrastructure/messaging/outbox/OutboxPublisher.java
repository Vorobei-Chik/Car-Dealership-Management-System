package study.project.dealership.infrastructure.messaging.outbox;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.project.dealership.common.observability.TraceContextPropagation;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);

    private final OutboxJpaRepository outboxJpaRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OpenTelemetry openTelemetry;

    @Scheduled(fixedDelayString = "${app.outbox.publish-delay-ms:1000}")
    @Transactional
    public void publishPending() {
        List<OutboxMessage> pending = outboxJpaRepository.findUnpublished();
        for (OutboxMessage message : pending) {
            try (Scope ignored = TraceContextPropagation.restore(openTelemetry, message.getTraceContext())) {
                kafkaTemplate.send(message.getTopic(), message.getMessageKey(), message.getPayload()).get();
                message.setPublishedAt(Instant.now());
                outboxJpaRepository.save(message);
                log.info("Published outbox message {} to topic {}", message.getId(), message.getTopic());
            } catch (Exception ex) {
                log.error("Failed to publish outbox message {}", message.getId(), ex);
            }
        }
    }
}
