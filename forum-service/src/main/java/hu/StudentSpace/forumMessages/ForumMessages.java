package hu.StudentSpace.forumMessages;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hu.StudentSpace.forum.Forum;
import hu.StudentSpace.forumLikesMessage.ForumLikesMessage;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
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
@Table(name = "forum_messages", schema = "forum_service")
public class ForumMessages implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(updatable = false)
    private UUID id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "forum_id", referencedColumnName = "id")
    private Forum forum;

    private String message;

    @Column(name = "sender_id", nullable = false, updatable = false)
    private String senderId;

    @Formula("(SELECT ue.username FROM keycloak.user_entity ue WHERE ue.id = sender_id)")
    private String senderName;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "answer_to_id")
    private ForumMessages answerTo;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    private boolean isDeleted = false;
    private Timestamp deletedAt;

    @OneToMany(mappedBy = "forumMessage", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ForumLikesMessage> likes;
}
