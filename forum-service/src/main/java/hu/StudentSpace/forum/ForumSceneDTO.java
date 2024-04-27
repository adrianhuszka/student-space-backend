package hu.StudentSpace.forum;

import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForumSceneDTO {
    private UUID id;
    private String name;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean isDeleted;
    private Timestamp deletedAt;
    private String lastMessage;
    private int unreadCount;
}
