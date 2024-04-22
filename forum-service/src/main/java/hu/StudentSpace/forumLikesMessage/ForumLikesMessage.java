package hu.StudentSpace.forumLikesMessage;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hu.StudentSpace.forumMessages.ForumMessages;
import jakarta.persistence.*;
import lombok.*;

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

    private String userId;
    private short liked;
}
