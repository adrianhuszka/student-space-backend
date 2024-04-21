package hu.StudentSpace.sceneUserMenbership;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hu.StudentSpace.keycloakHooks.UserEntityHook;
import hu.StudentSpace.scene.Scene;
import hu.StudentSpace.scene.SceneMembershipRole;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(SceneUserMembershipId.class)
@Table(name = "scene_user_membership", schema = "scene_service")
public class SceneUserMembership implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntityHook user;

    @Id
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "scene_id", referencedColumnName = "id")
    private Scene scene;

    @Enumerated(EnumType.STRING)
    private SceneMembershipRole role;
}
