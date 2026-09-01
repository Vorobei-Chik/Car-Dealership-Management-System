package study.project.dealership.application.services;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.project.dealership.abstractions.repository.UserRepository;
import study.project.dealership.application.exception.NotFoundException;
import study.project.dealership.contracts.user.UserService;
import study.project.dealership.contracts.user.model.CreatedUserCredentials;
import study.project.dealership.contracts.user.request.*;
import study.project.dealership.domain.user.User;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public CreatedUserCredentials createClient(@NotNull RequestCreateClient request) {
        return createUser(User.createClient());
    }

    @Override
    @Transactional
    public CreatedUserCredentials createManager(@NotNull RequestCreateManager request) {
        return createUser(User.createManager());
    }

    @Override
    @Transactional
    public CreatedUserCredentials createSystemAdmin(@NotNull RequestCreateSystemAdmin request) {
        return createUser(User.createSystemAdmin());
    }

    @Override
    @Transactional
    public CreatedUserCredentials createWarehouseAdmin(@NotNull RequestCreateWarehouseAdmin request) {
        return createUser(User.createWarehouseAdmin());
    }

    private CreatedUserCredentials createUser(User userTemplate) {
        String name = String.valueOf(System.currentTimeMillis());
        String password = "1234";
        User user = userRepository.add(userTemplate, name, password);
        return new CreatedUserCredentials(user, name, password);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUser(@NotNull RequestFindUser request) {
        return userRepository.find(request.userId())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    @Override
    @Transactional
    public void removeUser(@NotNull RequestRemoveUser request) {
        UUID userId = request.userId();
        if (userRepository.find(userId).isEmpty()) {
            throw new NotFoundException("User not found");
        }
        userRepository.remove(userId);
    }

    @Override
    @Transactional
    public User updateUser(@NotNull RequestUpdateUser request) {
        User user = userRepository.find(request.userId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setClient(request.isClient());
        user.setManager(request.isManager());
        user.setWarehouseAdmin(request.isWarehouseAdmin());
        user.setSystemAdmin(request.isAdmin());
        userRepository.update(user);

        return user;
    }
}
