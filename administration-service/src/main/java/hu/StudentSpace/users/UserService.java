package hu.StudentSpace.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final Keycloak keycloak;
    private final UserDTOMapper userDTOMapper;

    @Value("${app.keycloak-realm}")
    private String realm;

    @NotNull
    private static UserRepresentation createUserRepresentation(@NotNull UserRequest user) {
        final var credentials = new CredentialRepresentation();
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(user.password());
        credentials.setTemporary(false);

        final var createdUser = new UserRepresentation();
        createdUser.setUsername(user.username());
        createdUser.setEmail(user.email());
        createdUser.setFirstName(user.firstName());
        createdUser.setLastName(user.lastName());
        createdUser.setEnabled(true);
        createdUser.setAttributes(user.attributes());
        createdUser.setCredentials(List.of(credentials));

        return createdUser;
    }

    public int countUsers() {
        return keycloak.realm(realm).users().count();
    }

    public Set<UserDTO> listUsers(String search, int page, int size) {
        final var usersResource = keycloak.realm(realm).users();

        final var usersRep = usersResource.search(search, page, size, false);

        final Set<UserDTO> userList = new HashSet<>();

        usersRep.forEach(userRepresentation -> {
            userList.add(userDTOMapper.apply(usersResource.get(userRepresentation.getId())));
        });

        return userList;
    }

    public UserDTO getUserById(@NotNull String id) {
        return userDTOMapper.apply(keycloak.realm(realm).users().get(id));
    }

    public UserResponse createUser(@NotNull UserRequest user) {
        log.info("Creating user: username: {}, email: {}", user.username(), user.email());
        final var createdUser = createUserRepresentation(user);

        final var response = keycloak.realm(realm).users().create(createdUser);

        log.info("Response status: {} {}", response.getStatus(), response.getStatusInfo());

        return new UserResponse(
                response.getStatus(), response.readEntity(String.class)
        );
    }

    public void updateUser(@NotNull UserRequest user) {
        final var userRepresentation = keycloak.realm(realm).users().get(user.id()).toRepresentation();
        userRepresentation.setUsername(user.username());
        userRepresentation.setEmail(user.email());
        userRepresentation.setFirstName(user.firstName());
        userRepresentation.setLastName(user.lastName());
        userRepresentation.setAttributes(user.attributes());

        keycloak.realm(realm).users().get(user.id()).update(userRepresentation);
    }

    public UserResponse deleteUserById(@NotNull String id) {
        try (var response = keycloak.realm(realm).users().delete(id)) {
            log.info("Response status: {} {}", response.getStatus(), response.getStatusInfo());

            return new UserResponse(
                    response.getStatus(), response.readEntity(String.class)
            );
        }
    }
}
