package hu.StudentSpace.users;

import org.jetbrains.annotations.NotNull;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.function.Function;


@Service
public class UserReresentationDTOMapper implements Function<UserRepresentation, UserRepresentationDTO> {
    public UserRepresentationDTO apply(@NotNull UserRepresentation userResource) {
        return new UserRepresentationDTO(
                userResource.getId(),
                userResource.getUsername(),
                userResource.isEnabled(),
                userResource.getFirstName(),
                userResource.getLastName(),
                userResource.getEmail(),
                userResource.getAttributes(),
                userResource.getRealmRoles(),
                userResource.getGroups()
        );
    }
}
