package hu.StudentSpace.forum;

public record NewsRequest(
        String id,
        String name,
        String sceneId,
        String description
) {
}
