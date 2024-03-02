package hu.StudentSpace.groups;

import org.jetbrains.annotations.NotNull;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class GroupDTOForUsersMapper implements Function<GroupRepresentation, GroupDTOForUsers> {
    @Override
    public GroupDTOForUsers apply(@NotNull GroupRepresentation groupRepresentation) {
        return new GroupDTOForUsers(
                groupRepresentation.getId(),
                groupRepresentation.getName(),
                groupRepresentation.getPath()
        );
    }
}
