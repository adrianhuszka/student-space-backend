package hu.StudentSpace.users;

import java.util.List;
import java.util.Map;

public record UserRepresentationDTO(
        String id,
        String username,
        Boolean enabled,
        String firstName,
        String lastName,
        String email,
        Map<String, List<String>> attributes,
        List<String> realmRoles,
        List<String> groups) {
}
