package hu.StudentSpace.news;

public record NewsRequest(
        String id,
        String name,
        String sceneId,
        String description
) {
}
