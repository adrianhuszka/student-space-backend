package hu.StudentSpace.forumLikesMessage;

public record ForumLikesMessageRequest(
        String forumMessageId,
        String userId,
        boolean isLike
) {
}
