package study.project.dealership.contracts.request.request;

import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RequestFindRequest")
public record RequestFindRequest(@NotNull UUID id) {
}
