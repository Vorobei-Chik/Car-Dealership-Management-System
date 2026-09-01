package study.project.dealership.presentation.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import study.project.dealership.contracts.mapping.UserMapper;
import study.project.dealership.contracts.user.UserService;
import study.project.dealership.contracts.user.model.CreatedUserCredentials;
import study.project.dealership.contracts.user.model.UserDTO;
import study.project.dealership.contracts.user.model.UserWithCredentialsDTO;
import study.project.dealership.contracts.user.request.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/clients")
    public ResponseEntity<UserWithCredentialsDTO> createClient(@Valid @RequestBody RequestCreateClient request) {
        return ResponseEntity.ok(toCredentialsDto(userService.createClient(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/managers")
    public ResponseEntity<UserWithCredentialsDTO> createManager(@Valid @RequestBody RequestCreateManager request) {
        return ResponseEntity.ok(toCredentialsDto(userService.createManager(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/system-admins")
    public ResponseEntity<UserWithCredentialsDTO> createSystemAdmin(@Valid @RequestBody RequestCreateSystemAdmin request) {
        return ResponseEntity.ok(toCredentialsDto(userService.createSystemAdmin(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/warehouse-admins")
    public ResponseEntity<UserWithCredentialsDTO> createWarehouseAdmin(@Valid @RequestBody RequestCreateWarehouseAdmin request) {
        return ResponseEntity.ok(toCredentialsDto(userService.createWarehouseAdmin(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> dtos = userService.getAllUsers().stream()
                .map(UserMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> findUser(@PathVariable @NotNull UUID userId) {
        return ResponseEntity.ok(UserMapper.toDto(userService.findUser(new RequestFindUser(userId))));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeUser(@PathVariable @NotNull UUID userId) {
        userService.removeUser(new RequestRemoveUser(userId));
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody RequestUpdateUser request) {
        return ResponseEntity.ok(UserMapper.toDto(userService.updateUser(request)));
    }

    private static UserWithCredentialsDTO toCredentialsDto(CreatedUserCredentials created) {
        return UserMapper.toDtoWithCredentials(created.user(), created.username(), created.password());
    }
}
