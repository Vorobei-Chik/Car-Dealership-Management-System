package study.project.dealership.contracts.part.request;

import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RequestRemovePart")
public record RequestRemovePart(
        @NotNull UUID partId
) {}
