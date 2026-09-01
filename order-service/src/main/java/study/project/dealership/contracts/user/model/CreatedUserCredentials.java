package study.project.dealership.contracts.user.model;

import study.project.dealership.domain.user.User;

public record CreatedUserCredentials(User user, String username, String password) {
}
