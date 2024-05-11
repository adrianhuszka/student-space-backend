package hu.StudentSpace.newsMessages;

import hu.StudentSpace.newsLikesMessage.NewsLikesMessage;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public record NewsMessagesDTO(
        UUID id,
        String message,
        String senderId,
        String senderName,
        NewsMessages answerTo,
        Timestamp createdAt,
        Timestamp updatedAt,
        List<NewsLikesMessage> likes
) {
}
