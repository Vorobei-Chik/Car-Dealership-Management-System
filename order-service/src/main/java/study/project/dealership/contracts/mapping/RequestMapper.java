package study.project.dealership.contracts.mapping;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.contracts.request.model.RequestDTO;
import study.project.dealership.domain.request.RequestTestDrive;

public final class RequestMapper {

    private RequestMapper() {}

    @Contract("_, _ -> new")
    public static @NotNull RequestDTO toDto(@NotNull RequestTestDrive request, @NotNull CarDTO car) {
        return new RequestDTO(
                request.getId(),
                request.getClientId(),
                car,
                request.getDate()
        );
    }
}