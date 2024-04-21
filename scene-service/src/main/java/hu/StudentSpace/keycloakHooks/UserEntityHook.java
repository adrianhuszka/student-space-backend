package hu.StudentSpace.keycloakHooks;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hu.StudentSpace.sceneUserMenbership.SceneUserMembership;
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
@Table(name = "user_entity", schema = "keycloak")
public class UserEntityHook implements Serializable {
    @Id
    private String id;

    @JsonBackReference
    @OneToMany(mappedBy = "user")
    private List<SceneUserMembership> sceneUserMembership;
}
