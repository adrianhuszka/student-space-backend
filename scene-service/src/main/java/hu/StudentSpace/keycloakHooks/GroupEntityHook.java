package hu.StudentSpace.keycloakHooks;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hu.StudentSpace.sceneGroupMembership.SceneGroupMembership;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "keycloak_group", schema = "keycloak")
public class GroupEntityHook implements Serializable {
    @Id
    private String id;

    @JsonBackReference
    @OneToMany(mappedBy = "group")
    private List<SceneGroupMembership> sceneGroupMembership;
}
