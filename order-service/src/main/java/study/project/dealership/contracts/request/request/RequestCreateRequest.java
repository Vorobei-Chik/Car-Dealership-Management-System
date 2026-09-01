package study.project.dealership.contracts.request.request;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RequestCreateRequest")
public record RequestCreateRequest(
        @NotNull UUID carId,
        @NotNull LocalDateTime date
) {}
