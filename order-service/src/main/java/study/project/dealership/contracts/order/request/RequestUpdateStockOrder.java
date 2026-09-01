package study.project.dealership.contracts.order.request;

import java.util.UUID;

import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.media.Schema;
import study.project.dealership.contracts.order.model.status.StockOrderStatusDTO;

@Schema(name = "RequestUpdateStockOrder")
public record RequestUpdateStockOrder(
        @NotNull UUID id,
        @NotNull UUID clientId,
        @NotNull UUID managerId,
        @NotNull UUID carId,
        @NotNull StockOrderStatusDTO status
) {
}
