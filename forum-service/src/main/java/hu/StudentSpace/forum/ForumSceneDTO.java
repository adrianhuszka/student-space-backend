package hu.StudentSpace.forum;

import java.sql.Timestamp;
import java.util.UUID;

public record ForumSceneDTO(
        UUID id,
        String name,
        Timestamp createdAt,
        Timestamp updatedAt,
        boolean isDeleted,
        Timestamp deletedAt,
        String lastMessage
) {
}
