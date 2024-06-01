package hu.StudentSpace.forumLikesMessage;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hu.StudentSpace.forumMessages.ForumMessages;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "forum_likes", schema = "forum_service")
public class ForumLikesMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "forum_message_id")
    private ForumMessages forumMessage;

    @Column(name = "user_id")
    private String userId;

    @Formula("(SELECT ue.username FROM keycloak.user_entity ue WHERE ue.id = user_id)")
    private String userName;

    @Formula("(SELECT CONCAT(ue.first_name, ' ', ue.last_name) FROM keycloak.user_entity ue WHERE ue.id = user_id)")
    private String name;

    @Enumerated(EnumType.STRING)
    private LikedEnum liked;
}
