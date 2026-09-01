package study.project.dealership.domain.user;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Setter
public class User {
    private UUID id;
    private boolean client;
    private boolean manager;
    private boolean warehouseAdmin;
    private boolean systemAdmin;

    @Contract(" -> new")
    public static @NotNull User createClient() {
        User user = new User();
        user.setClient(true);
        return user;
    }

    @Contract(" -> new")
    public static @NotNull User createManager() {
        User user = new User();
        user.setManager(true);
        user.setClient(true);
        return user;
    }

    @Contract(" -> new")
    public static @NotNull User createWarehouseAdmin() {
        User user = new User();
        user.setWarehouseAdmin(true);
        user.setClient(true);
        return user;
    }

    @Contract(" -> new")
    public static @NotNull User createSystemAdmin() {
        User user = new User();
        user.setClient(true);
        user.setManager(true);
        user.setWarehouseAdmin(true);
        user.setSystemAdmin(true);
        return user;
    }
}