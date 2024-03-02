package hu.StudentSpace.users;


import hu.StudentSpace.groups.GroupDTOForUsersMapper;
import hu.StudentSpace.roles.RolesDTOMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.resource.UserResource;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserDTOMapper implements Function<UserResource, UserDTO> {
    private final RolesDTOMapper rolesDTOMapper;
    private final GroupDTOForUsersMapper groupDTOForUsersMapper;

    @Override
    public UserDTO apply(@NotNull UserResource userResource) {
        return new UserDTO(
                userResource.toRepresentation().getId(),
                userResource.toRepresentation().getUsername(),
                userResource.toRepresentation().isEnabled(),
                userResource.toRepresentation().getFirstName() + " " + userResource.toRepresentation().getLastName(),
                userResource.toRepresentation().getEmail(),
                userResource.toRepresentation().getAttributes(),
                userResource.roles().realmLevel().listAll().stream().map(rolesDTOMapper).toList(),
                userResource.groups().stream().map(groupDTOForUsersMapper).toList()
        );
    }
}
