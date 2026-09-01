package study.project.dealership.contracts.user.model;

import java.util.UUID;

public record UserWithCredentialsDTO(
        UUID id,
        boolean isClient,
        boolean isManager,
        boolean isWarehouseAdmin,
        boolean isSystemAdmin,
        String username,
        String password
) {}