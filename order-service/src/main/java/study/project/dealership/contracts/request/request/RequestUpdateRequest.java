package study.project.dealership.contracts.request.request;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RequestUpdateRequest")
public record RequestUpdateRequest(
        @NotNull UUID id,
        @NotNull UUID clientId,
        @NotNull UUID carId,
        @NotNull LocalDateTime date
) {
}
