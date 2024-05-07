package hu.StudentSpace.forum;

import hu.StudentSpace.forumMessages.ForumMessages;
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
@Table(name = "forum", schema = "forum_service")
public class Forum implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;
    private String description;

    @Column(nullable = false)
    private UUID sceneId;

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ForumMessages> messages;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
    
    private boolean isDeleted;
    private Timestamp deletedAt;
}
