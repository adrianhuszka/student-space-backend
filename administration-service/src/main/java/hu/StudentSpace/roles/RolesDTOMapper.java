package hu.StudentSpace.roles;

import org.jetbrains.annotations.NotNull;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class RolesDTOMapper implements Function<RoleRepresentation, RolesDTO> {
    @Override
    public RolesDTO apply(@NotNull RoleRepresentation roleRepresentation) {
        return new RolesDTO(
                roleRepresentation.getId(),
                roleRepresentation.getName(),
                roleRepresentation.getDescription(),
                roleRepresentation.isComposite()
        );
    }
}
