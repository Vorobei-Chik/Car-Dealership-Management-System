package study.project.dealership.contracts.user.model;

import java.util.UUID;

public record UserDTO(
        UUID id,
        boolean isClient,
        boolean isManager,
        boolean isWarehouseAdmin,
        boolean isSystemAdmin
) { }