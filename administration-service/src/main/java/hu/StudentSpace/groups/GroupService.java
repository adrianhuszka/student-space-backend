package hu.StudentSpace.groups;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final Keycloak keycloak;
    private final GroupDTOMapper groupDTOMapper;

    @Value("${app.keycloak-realm}")
    private String realm;

    public List<GroupDTO> listGroups() {
        final var groups = keycloak.realm(realm).groups().groups();

        List<GroupDTO> groupList = new ArrayList<>();

        for (GroupRepresentation group : groups) {
            GroupDTO groupDTO = getGroupById(group.getId());
            groupList.add(groupDTO);
        }

        return groupList;
    }

    public GroupDTO getGroupById(String id) {
        return groupDTOMapper.apply(keycloak.realm(realm).groups().group(id));
    }

    public void createGroup(@NotNull final GroupRequest request) {
        final var createdGroup = new GroupRepresentation();
        createdGroup.setName(request.name());
        createdGroup.setPath(request.path());

        keycloak.realm(realm).groups().add(createdGroup);
    }

    public void updateGroup(@NotNull final GroupRequest request) {
        final var group = keycloak.realm(realm).groups().group(request.id()).toRepresentation();
        group.setName(request.name());
        group.setPath(request.path());

        keycloak.realm(realm).groups().group(request.id()).update(group);
    }

    public void deleteGroupById(String id) {
        keycloak.realm(realm).groups().group(id).remove();
    }
}
