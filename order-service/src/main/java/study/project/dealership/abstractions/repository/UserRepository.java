package study.project.dealership.abstractions.repository;

import study.project.dealership.domain.user.User;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User add(User user, String name, String password);
    void update(User user);
    void remove(UUID id);
    Optional<User> find(UUID id);
    List<User> getAll();
    Optional<User> getRandomManager();
}
