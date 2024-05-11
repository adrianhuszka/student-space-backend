package hu.StudentSpace.newsMessages;

public record NewsMessagesRequest(
        String id,
        String newsId,
        String message,
        String senderId,
        String answerToId
) {
}
