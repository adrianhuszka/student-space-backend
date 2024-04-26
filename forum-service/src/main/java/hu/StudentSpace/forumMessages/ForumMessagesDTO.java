package hu.StudentSpace.forumMessages;

import hu.StudentSpace.forumLikesMessage.ForumLikesMessage;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public record ForumMessagesDTO(
        UUID id,
        String message,
        String senderId,
        String senderName,
        ForumMessages answerTo,
        Timestamp createdAt,
        Timestamp updatedAt,
        List<ForumLikesMessage> likes
) {
}
