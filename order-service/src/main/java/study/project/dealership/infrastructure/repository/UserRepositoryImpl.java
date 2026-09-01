package study.project.dealership.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import study.project.dealership.abstractions.repository.UserRepository;
import study.project.dealership.domain.user.User;

import jakarta.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final Keycloak keycloak;
    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public User add(@NonNull User user, String name, String password) {
        UserRepresentation userRep = new UserRepresentation();
        userRep.setUsername(name);
        userRep.setEnabled(true);
        userRep.setEmail(name + "@example.com");
        userRep.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        userRep.setCredentials(List.of(credential));

        String keycloakUserId;
        try (Response response = keycloak.realm(realm).users().create(userRep)) {
            if (response.getStatus() != 201) {
                String errorBody = response.readEntity(String.class);
                throw new RuntimeException("User creation failed, status: " + response.getStatus() + ", error: " + errorBody);
            }
            keycloakUserId = response.getLocation().getPath()
                    .replaceAll(".*/([^/]+)$", "$1");
        }

        user.setId(UUID.fromString(keycloakUserId));
        assignRealmRoles(keycloakUserId, user);

        return user;
    }

    @Override
    public void update(User user) {
        assignRealmRoles(user.getId().toString(), user);
    }

    @Override
    public void remove(@NonNull UUID id) {
        keycloak.realm(realm).users().delete(id.toString());
    }

    @Override
    public Optional<User> find(UUID id) {
        try {
            UserRepresentation rep = keycloak.realm(realm).users().get(id.toString()).toRepresentation();
            if (rep == null) return Optional.empty();
            User user = extractUser(rep);
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAll() {
        List<UserRepresentation> reps = keycloak.realm(realm).users().list();
        return reps.stream()
                .map(this::extractUser)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> getRandomManager() {
        List<User> managers = getAll().stream()
                .filter(User::isManager)
                .toList();
        if (managers.isEmpty()) {
            return Optional.empty();
        }
        Random random = new Random();
        return Optional.of(managers.get(random.nextInt(managers.size())));
    }

    private void assignRealmRoles(String userId, @NonNull User user) {
        List<String> roles = new ArrayList<>();
        if (user.isClient()) roles.add("USER");
        if (user.isManager()) roles.add("MANAGER");
        if (user.isWarehouseAdmin()) roles.add("WAREHOUSE_ADMIN");
        if (user.isSystemAdmin()) roles.add("ADMIN");

        keycloak.realm(realm).users().get(userId).roles().realmLevel()
                .add(keycloak.realm(realm).roles().list().stream()
                        .filter(r -> roles.contains(r.getName()))
                        .collect(Collectors.toList()));
    }

    private @NonNull User extractUser(@NonNull UserRepresentation rep) {
        User user = new User();
        user.setId(UUID.fromString(rep.getId()));
        List<String> realmRoles = keycloak.realm(realm).users()
                .get(rep.getId()).roles().realmLevel().listEffective().stream()
                .map(RoleRepresentation::getName)
                .toList();
        user.setClient(realmRoles.contains("USER"));
        user.setManager(realmRoles.contains("MANAGER"));
        user.setWarehouseAdmin(realmRoles.contains("WAREHOUSE_ADMIN"));
        user.setSystemAdmin(realmRoles.contains("ADMIN"));
        return user;
    }
}