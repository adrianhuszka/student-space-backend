package hu.StudentSpace.forumMessages;

public record ForumMessagesRequest(
        String id,
        String forumId,
        String message,
        String senderId,
        String answerToId
) {
}
