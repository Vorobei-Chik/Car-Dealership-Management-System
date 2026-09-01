package study.project.dealership.contracts.assembly.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(name = "RequestUpdateAssemblyOrder")
public record RequestUpdateAssemblyOrder(
        @NotNull UUID id,
        UUID warehouseAdminId,
        @NotNull String status
) {
}
