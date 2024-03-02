package hu.StudentSpace.users;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<UserRepresentation, UserDTO> {
    @Override
    public UserDTO apply(UserRepresentation userRepresentation) {
        return new UserDTO(
                userRepresentation.getId(),
                userRepresentation.getUsername(),
                userRepresentation.getEmail(),
                userRepresentation.getFirstName(),
                userRepresentation.getLastName()
        );
    }
}
