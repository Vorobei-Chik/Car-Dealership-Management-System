package study.project.dealership.contracts.mapping;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.contracts.user.model.UserDTO;
import study.project.dealership.contracts.user.model.UserWithCredentialsDTO;
import study.project.dealership.domain.user.User;

public final class UserMapper {

    private UserMapper() {}

    @Contract("_ -> new")
    public static @NotNull UserDTO toDto(@NotNull User user) {
        return new UserDTO(
                user.getId(),
                user.isClient(),
                user.isManager(),
                user.isWarehouseAdmin(),
                user.isSystemAdmin()
        );
    }

    public static @NotNull UserWithCredentialsDTO toDtoWithCredentials(@NotNull User user, String username, String password) {
        return new UserWithCredentialsDTO(
                user.getId(),
                user.isClient(),
                user.isManager(),
                user.isWarehouseAdmin(),
                user.isSystemAdmin(),
                username,
                password
        );
    }
}
