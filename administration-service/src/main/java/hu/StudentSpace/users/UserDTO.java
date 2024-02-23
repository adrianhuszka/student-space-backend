package hu.StudentSpace.users;

public record UserDTO(
        String id,
        String username,
        String email,
        String firstName,
        String lastName
) {
}
