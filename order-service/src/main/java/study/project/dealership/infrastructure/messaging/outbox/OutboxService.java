package study.project.dealership.infrastructure.messaging.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.OpenTelemetry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.project.dealership.common.observability.TraceContextPropagation;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxJpaRepository outboxJpaRepository;
    private final ObjectMapper objectMapper;
    private final OpenTelemetry openTelemetry;

    @Transactional
    public void enqueue(String topic, String messageKey, Object payload, UUID traceId) {
        try {
            OutboxMessage message = new OutboxMessage();
            message.setTopic(topic);
            message.setMessageKey(messageKey);
            message.setPayload(objectMapper.writeValueAsString(payload));
            message.setTraceId(traceId);
            message.setTraceContext(TraceContextPropagation.serialize(openTelemetry));
            message.setCreatedAt(Instant.now());
            outboxJpaRepository.save(message);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize outbox payload", e);
        }
    }
}
