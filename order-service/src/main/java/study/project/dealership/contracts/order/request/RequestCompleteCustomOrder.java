package study.project.dealership.contracts.order.request;

import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RequestCompleteCustomOrder")
public record RequestCompleteCustomOrder(
        @NotNull UUID orderId
) {}
