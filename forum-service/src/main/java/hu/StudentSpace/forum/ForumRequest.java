package hu.StudentSpace.forum;

public record ForumRequest(
        String id,
        String name,
        String sceneId,
        String description
) {
}
