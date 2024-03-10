package hu.StudentSpace.roles;

import hu.StudentSpace.error.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolesService {
    private final Keycloak keycloak;

    public List<RoleRepresentation> listRoles() {
        return keycloak.realm("StudentSpace").roles().list();
    }

    public void updateRolesOnUser(@NotNull RolesRequest request) {
        final var user = Optional.of(
                keycloak.realm("StudentSpace").users().get(request.id())
        ).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        request.roleNames().forEach(role -> {
            final var roleResource = Optional.of(
                    keycloak.realm("StudentSpace").roles().get(role)
            ).orElseThrow(
                    () -> new ResourceNotFoundException("Role not found")
            );

            user.roles().realmLevel().add(List.of(roleResource.toRepresentation()));
        });
    }

    public void updateRolesOnGroup(@NotNull RolesRequest request) {
        final var group = Optional.of(
                keycloak.realm("StudentSpace").groups().group(request.id())
        ).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        request.roleNames().forEach(role -> {
            final var roleRepresentation = Optional.of(
                    keycloak.realm("StudentSpace").roles().get(role)
            ).orElseThrow(
                    () -> new ResourceNotFoundException("Role not found")
            );

            group.roles().realmLevel().add(List.of(roleRepresentation.toRepresentation()));
        });
    }
}
