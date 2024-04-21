package hu.StudentSpace.sceneUserMenbership;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public class SceneUserMembershipId implements Serializable {
    private String user;
    private UUID scene;
}
