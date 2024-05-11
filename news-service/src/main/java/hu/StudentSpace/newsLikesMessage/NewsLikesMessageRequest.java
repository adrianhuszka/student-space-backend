package hu.StudentSpace.newsLikesMessage;

public record NewsLikesMessageRequest(
        String newsMessageId,
        String userId,
        boolean isLike
) {
}
