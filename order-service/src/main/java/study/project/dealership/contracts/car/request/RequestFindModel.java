package study.project.dealership.contracts.car.request;

import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RequestFindModel")
public record RequestFindModel(@NotNull UUID id) {
}
