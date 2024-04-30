package hu.StudentSpace.forum;

import hu.StudentSpace.forumMessageRead.ForumMessageReadService;
import hu.StudentSpace.forumMessages.ForumMessages;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.function.Function;

@Service
public class ForumSceneDTOMapper implements Function<Forum, ForumSceneDTO> {
    @Override
    public ForumSceneDTO apply(@NotNull Forum forum) {
        return ForumSceneDTO.builder()
                .id(forum.getId())
                .name(forum.getName())
                .description(forum.getDescription())
                .createdAt(forum.getCreatedAt())
                .updatedAt(forum.getUpdatedAt())
                .isDeleted(forum.isDeleted())
                .deletedAt(forum.getDeletedAt())
                .lastMessage(forum.getMessages().stream()
                        .filter(message -> !message.isDeleted())
                        .max(Comparator.comparing(ForumMessages::getCreatedAt))
                        .map(ForumMessages::getMessage)
                        .orElse(null))
                .build();
    }

    public ForumSceneDTO apply(@NotNull Forum forum, int count) {
        return ForumSceneDTO.builder()
                .id(forum.getId())
                .name(forum.getName())
                .description(forum.getDescription())
                .createdAt(forum.getCreatedAt())
                .updatedAt(forum.getUpdatedAt())
                .isDeleted(forum.isDeleted())
                .deletedAt(forum.getDeletedAt())
                .lastMessage(forum.getMessages().stream()
                        .filter(message -> !message.isDeleted())
                        .max(Comparator.comparing(ForumMessages::getCreatedAt))
                        .map(ForumMessages::getMessage)
                        .orElse(null)
                )
                .lastMessageCreatedAt(forum.getMessages().stream()
                        .max(Comparator.comparing(ForumMessages::getCreatedAt))
                        .map(ForumMessages::getCreatedAt).orElse(null)
                )
                .messageCount(forum.getMessages().size())
                .unreadCount(count)
                .build();
    }
}
