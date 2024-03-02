package hu.StudentSpace.users;

public record UserDTOForGroups(
        String id,
        String username,
        String email,
        String firstName,
        String lastName
) {
}
