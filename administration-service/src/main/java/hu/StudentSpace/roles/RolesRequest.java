package hu.StudentSpace.roles;

import java.util.List;

public record RolesRequest(
        String id,
        List<String> roleNames
) {
}
