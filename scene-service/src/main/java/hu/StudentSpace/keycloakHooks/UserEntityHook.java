package hu.StudentSpace.keycloakHooks;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hu.StudentSpace.sceneUserMenbership.SceneUserMembership;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_entity", schema = "keycloak")
public class UserEntityHook implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Formula("CONCAT(first_name, ' ', last_name)")
    private String fullName;

    @JsonBackReference
    @OneToMany(mappedBy = "user")
    private List<SceneUserMembership> sceneUserMembership;
}
