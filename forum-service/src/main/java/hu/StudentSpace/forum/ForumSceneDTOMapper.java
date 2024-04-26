package hu.StudentSpace.forum;

import hu.StudentSpace.forumMessages.ForumMessages;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.function.Function;

@Service
public class ForumSceneDTOMapper implements Function<Forum, ForumSceneDTO> {
    @Override
    public ForumSceneDTO apply(Forum forum) {
        return new ForumSceneDTO(
                forum.getId(),
                forum.getName(),
                forum.getCreatedAt(),
                forum.getUpdatedAt(),
                forum.isDeleted(),
                forum.getDeletedAt(),
                forum.getMessages().stream()
                        .filter(message -> !message.isDeleted())
                        .max(Comparator.comparing(ForumMessages::getCreatedAt))
                        .map(ForumMessages::getMessage)
                        .orElse(null)
        );
    }
}
