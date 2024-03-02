package hu.StudentSpace.users;

import java.util.List;
import java.util.Map;

public record UserRequest(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        String password,
        Map<String, List<String>> attributes
) {
}
