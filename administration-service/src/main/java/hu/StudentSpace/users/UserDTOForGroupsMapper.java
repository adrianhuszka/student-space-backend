package hu.StudentSpace.users;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOForGroupsMapper implements Function<UserRepresentation, UserDTOForGroups> {
    @Override
    public UserDTOForGroups apply(UserRepresentation userRepresentation) {
        return new UserDTOForGroups(
                userRepresentation.getId(),
                userRepresentation.getUsername(),
                userRepresentation.getEmail(),
                userRepresentation.getFirstName(),
                userRepresentation.getLastName()
        );
    }
}
