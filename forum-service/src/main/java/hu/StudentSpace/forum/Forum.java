package hu.StudentSpace.forum;

import hu.StudentSpace.forumMessages.ForumMessages;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
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

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private UUID sceneId;

    @OneToMany(mappedBy = "forum")
    private List<ForumMessages> messages;
}
