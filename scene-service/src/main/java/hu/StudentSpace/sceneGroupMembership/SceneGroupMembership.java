package hu.StudentSpace.sceneGroupMembership;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hu.StudentSpace.keycloakHooks.GroupEntityHook;
import hu.StudentSpace.scene.Scene;
import hu.StudentSpace.scene.SceneMembershipRole;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(SceneGroupMembershipId.class)
@Table(name = "scene_group_membership", schema = "scene_service")
public class SceneGroupMembership {
    @Id
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private GroupEntityHook group;

    @Id
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "scene_id", referencedColumnName = "id")
    private Scene scene;

    @Enumerated(EnumType.STRING)
    private SceneMembershipRole role;
}
