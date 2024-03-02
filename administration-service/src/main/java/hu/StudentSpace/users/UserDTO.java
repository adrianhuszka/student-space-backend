package hu.StudentSpace.users;

import hu.StudentSpace.groups.GroupDTOForUsers;
import hu.StudentSpace.roles.RolesDTO;

import java.util.List;
import java.util.Map;

public record UserDTO(
        String id,
        String username,
        Boolean enabled,
        String name,
        String email,
        Map<String, List<String>> attributes,
        List<RolesDTO> realmRoles,
        List<GroupDTOForUsers> groups
) {
}
