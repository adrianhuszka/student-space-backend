package hu.StudentSpace.scene;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hu.StudentSpace.sceneGroupMembership.SceneGroupMembership;
import hu.StudentSpace.sceneItems.SceneItems;
import hu.StudentSpace.sceneItems.SceneReturnItems;
import hu.StudentSpace.sceneUserMenbership.SceneUserMembership;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "scene", schema = "scene_service")
public class Scene implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;
    private String description;

    private Boolean archived = false;
    private Boolean deleted = false;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_scene_id")
    private Scene parentScene;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
    private Timestamp archivedAt;
    private Timestamp deletedAt;

    @Column(nullable = false)
    private String ownerId;

    @OneToMany(mappedBy = "scene", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<SceneUserMembership> sceneUserMembership;

    @JsonBackReference
    @OneToMany(mappedBy = "scene", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SceneGroupMembership> sceneGroupMembership;

    @JsonBackReference
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "scene_items", schema = "scene_service", joinColumns = @JoinColumn(name = "scene_id"))
    private List<SceneItems> dbItems;

    @Transient
    private List<SceneReturnItems> items;
}
