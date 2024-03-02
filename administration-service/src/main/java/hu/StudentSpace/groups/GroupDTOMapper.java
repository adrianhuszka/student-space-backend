package hu.StudentSpace.groups;

import hu.StudentSpace.users.UserDTOForGroupsMapper;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.GroupResource;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GroupDTOMapper implements Function<GroupResource, GroupDTO> {
    private final UserDTOForGroupsMapper userDTOForGroupsMapper;

    @Override
    public GroupDTO apply(GroupResource groupResource) {
        return new GroupDTO(
                groupResource.toRepresentation().getId(),
                groupResource.toRepresentation().getName(),
                groupResource.toRepresentation().getPath(),
                groupResource.toRepresentation().getParentId(),
                groupResource.toRepresentation().getSubGroups().size(),
                groupResource.toRepresentation().getSubGroups(),
                groupResource.toRepresentation().getAttributes(),
                groupResource.toRepresentation().getRealmRoles(),
                groupResource.toRepresentation().getClientRoles(),
                groupResource.toRepresentation().getAccess(),
                groupResource.members() != null ? groupResource.members().stream().map(userDTOForGroupsMapper).toList() : null
        );
    }
}
