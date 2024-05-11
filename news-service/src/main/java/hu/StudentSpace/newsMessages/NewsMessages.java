package hu.StudentSpace.newsMessages;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hu.StudentSpace.news.News;
import hu.StudentSpace.newsLikesMessage.NewsLikesMessage;
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
@Table(name = "news_messages", schema = "news_service")
public class NewsMessages implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(updatable = false)
    private UUID id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "news_id", referencedColumnName = "id")
    private News news;

    private String message;

    @Column(name = "sender_id", nullable = false, updatable = false)
    private String senderId;

    @Formula("(SELECT ue.username FROM keycloak.user_entity ue WHERE ue.id = sender_id)")
    private String senderName;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "answer_to_id")
    private NewsMessages answerTo;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    private boolean isDeleted = false;
    private Timestamp deletedAt;

    @OneToMany(mappedBy = "newsMessage", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<NewsLikesMessage> likes;
}
