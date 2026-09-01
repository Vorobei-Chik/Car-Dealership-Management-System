package study.project.dealership.common.observability;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapSetter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class TraceContextPropagation {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, String>> MAP_TYPE = new TypeReference<>() {};
    private static final TextMapGetter<Map<String, String>> CARRIER_GETTER = new TextMapGetter<>() {
        @Override
        public Iterable<String> keys(@NotNull Map<String, String> carrier) {
            return carrier.keySet();
        }

        @Override
        public @Nullable String get(Map<String, String> carrier, @NotNull String key) {
            assert carrier != null;
            return carrier.get(key);
        }
    };
    private static final TextMapSetter<Map<String, String>> CARRIER_SETTER = (carrier, key, value) -> {
        assert carrier != null;
        carrier.put(key, value);
    };

    private TraceContextPropagation() {
    }

    public static @Nullable String serialize(@NotNull OpenTelemetry openTelemetry) {
        Map<String, String> carrier = new HashMap<>();
        openTelemetry.getPropagators().getTextMapPropagator().inject(Context.current(), carrier, CARRIER_SETTER);
        if (carrier.isEmpty()) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(carrier);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize trace context", e);
        }
    }

    public static @NotNull Scope restore(@NotNull OpenTelemetry openTelemetry, @Nullable String serialized) {
        if (serialized == null || serialized.isBlank()) {
            return Context.root().makeCurrent();
        }
        try {
            Map<String, String> carrier = MAPPER.readValue(serialized, MAP_TYPE);
            Context context = openTelemetry.getPropagators().getTextMapPropagator()
                    .extract(Context.root(), carrier, CARRIER_GETTER);
            return context.makeCurrent();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialize trace context", e);
        }
    }
}
