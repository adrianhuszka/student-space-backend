package hu.StudentSpace.groups;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final Keycloak keycloak;
    private final GroupDTOMapper groupDTOMapper;

    @Value("${app.keycloak-realm}")
    private String realm;

    public List<GroupRepresentation> listGroups() {
        return keycloak.realm(realm).groups().groups();
    }

    public GroupDTO getGroupById(String id) {
        return groupDTOMapper.apply(keycloak.realm(realm).groups().group(id));
    }

    public void createGroup(GroupRequest request) {
        var createdGroup = new GroupRepresentation();
        createdGroup.setName(request.name());
        createdGroup.setPath(request.path());

        keycloak.realm(realm).groups().add(createdGroup);
    }

    public void updateGroup(GroupRequest request) {
        var group = keycloak.realm(realm).groups().group(request.id()).toRepresentation();
        group.setName(request.name());
        group.setPath(request.path());

        keycloak.realm(realm).groups().group(request.id()).update(group);
    }

    public void deleteGroupById(String id) {
        keycloak.realm(realm).groups().group(id).remove();
    }
}
