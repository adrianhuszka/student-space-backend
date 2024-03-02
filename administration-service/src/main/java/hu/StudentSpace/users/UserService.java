package hu.StudentSpace.users;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final Keycloak keycloak;
    private final UserDTOMapper userDTOMapper;
    private final UserRepresentationDTOMapper userRepresentationDTOMapper;

    @Value("${app.keycloak-realm}")
    private String realm;

    public Set<UserDTO> listUsers(String search, int page, int size) {
        final var usersResource = keycloak.realm(realm).users();
        final var usersRep = usersResource.search(search, page, size);

        final Set<UserDTO> userList = new HashSet<>();

        usersRep.forEach(userRepresentation -> {
            userList.add(userDTOMapper.apply(usersResource.get(userRepresentation.getId())));
        });

        return userList;
    }

    public UserDTO getUserById(@NotNull String id) {
        return userDTOMapper.apply(keycloak.realm(realm).users().get(id));
    }

//    public void createUser(UserRepresentation user) {
//        keycloak.realm(realm).users().create(user);
//    }
}
