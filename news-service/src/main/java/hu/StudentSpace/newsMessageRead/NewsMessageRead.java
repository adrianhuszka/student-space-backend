package hu.StudentSpace.newsMessageRead;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(NewsMessageReadId.class)
@Table(name = "news_message_read", schema = "news_service")
public class NewsMessageRead implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID messageId;

    @Id
    private String userId;
}
