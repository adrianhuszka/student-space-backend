package hu.StudentSpace.groups;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {
    private final Keycloak keycloak;
    private final GroupDTOMapper groupDTOMapper;

    @Value("${app.keycloak-realm}")
    private String realm;

    public List<GroupDTO> listGroups(String search, int page, int size) {
        final var groupsResource = keycloak.realm(realm).groups();
        final var groups = groupsResource.groups(search, page, size);

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

    public GroupResponse createGroup(@NotNull final GroupRequest request) {
        log.info("Creating group: name: {}, parentId: {}", request.name(), request.parentId());

        final var createdGroup = new GroupRepresentation();
        createdGroup.setName(request.name());

        if (request.parentId() != null) {
            createdGroup.setParentId(request.parentId());
        }

        final var response = keycloak.realm(realm).groups().add(createdGroup);

        log.info("Response status: {} {}", response.getStatus(), response.getStatusInfo());

        return new GroupResponse(
                response.getStatus(), response.readEntity(String.class)
        );
    }

    public void updateGroup(@NotNull final GroupRequest request) {
        final var group = keycloak.realm(realm).groups().group(request.id()).toRepresentation();
        group.setName(request.name());
        group.setParentId(request.parentId());

        keycloak.realm(realm).groups().group(request.id()).update(group);
    }

    public void deleteGroupById(String id) {
        keycloak.realm(realm).groups().group(id).remove();
    }
}
