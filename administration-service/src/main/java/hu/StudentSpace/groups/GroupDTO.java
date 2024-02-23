package hu.StudentSpace.groups;

import hu.StudentSpace.users.UserDTO;
import org.keycloak.representations.idm.GroupRepresentation;

import java.util.List;
import java.util.Map;

public record GroupDTO(
        String id,
        String name,
        String path,
        String parentId,
        int subGroupCount,
        List<GroupRepresentation> subGroups,
        Map<String, List<String>> attributes,
        List<String> realmRoles,
        Map<String, List<String>> clientRoles,
        Map<String, Boolean> access,
        List<UserDTO> members

) {
}
