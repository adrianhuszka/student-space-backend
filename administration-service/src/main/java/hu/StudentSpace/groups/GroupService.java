package hu.StudentSpace.groups;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {
    private final Keycloak keycloak;
    private final GroupDTOMapper groupDTOMapper;

    @Value("${app.keycloak-realm}")
    private String realm;

    public int countGroups() {
        log.info("Counting groups: {}", keycloak.realm(realm).groups().count());
        return 1;
    }

    public List<GroupRepresentation> listGroups(String search, int page, int size) {
        final var groupsResource = keycloak.realm(realm).groups();

        return groupsResource.groups(search, page, size, false);
    }

    public List<String> getAllJoinedGroups(String userId) {
        return keycloak.realm(realm).users().get(userId).groups().stream().map(GroupRepresentation::getId).collect(Collectors.toList());
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
