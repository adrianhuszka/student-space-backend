package hu.StudentSpace.sceneGroupMembership;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public class SceneGroupMembershipId implements Serializable {
    private String group;
    private UUID scene;
}
