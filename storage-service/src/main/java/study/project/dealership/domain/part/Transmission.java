package study.project.dealership.domain.part;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.domain.valueobject.Money;
import study.project.dealership.domain.valueobject.carinfo.VehicleDriveType;

@Setter
@Getter
@Entity
@Table(name = "transmissions")
@DiscriminatorValue("TRANSMISSION")
public class Transmission extends Part {

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_drive_type", nullable = false)
    private VehicleDriveType vehicleDriveType;

    @Contract("_, _ -> new")
    public static @NotNull Transmission create(
            VehicleDriveType vehicleDriveType,
            Money price
    ) {
        Transmission carTransmission = new Transmission();
        carTransmission.setVehicleDriveType(vehicleDriveType);
        carTransmission.setPrice(price);

        return carTransmission;
    }
}
