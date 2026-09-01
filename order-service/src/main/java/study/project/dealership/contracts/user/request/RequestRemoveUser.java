package study.project.dealership.contracts.user.request;

import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RequestRemoveUser")
public record RequestRemoveUser(
        @NotNull UUID userId
) {}
