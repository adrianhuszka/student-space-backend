package hu.StudentSpace.forumMessages;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ForumMessagesDTOMapper implements Function<ForumMessages, ForumMessagesDTO> {
    @Override
    public ForumMessagesDTO apply(@NotNull final ForumMessages forumMessages) {
        return new ForumMessagesDTO(
                forumMessages.getId(),
                forumMessages.getMessage(),
                forumMessages.getSenderId(),
                forumMessages.getSenderName(),
                forumMessages.getAnswerTo(),
                forumMessages.getCreatedAt(),
                forumMessages.getUpdatedAt(),
                forumMessages.getLikes()
        );
    }
}
