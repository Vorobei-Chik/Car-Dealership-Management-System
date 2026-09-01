package study.project.dealership.domain.request;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.domain.BaseEntity;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "requests")
public class RequestTestDrive extends BaseEntity {

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @Column(name = "car_id", nullable = false)
    private UUID carId;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    public static @NotNull RequestTestDrive create(
            @NotNull UUID clientId,
            @NotNull UUID carId,
            @NotNull LocalDateTime date
    ) {
        if (date.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Test drive date cannot be in the past");
        }

        RequestTestDrive carRequest = new RequestTestDrive();
        carRequest.setClientId(clientId);
        carRequest.setCarId(carId);
        carRequest.setDate(date);
        return carRequest;
    }
}