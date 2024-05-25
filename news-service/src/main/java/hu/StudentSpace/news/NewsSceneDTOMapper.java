package hu.StudentSpace.news;

import hu.StudentSpace.newsMessages.NewsMessages;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.function.Function;

@Service
public class NewsSceneDTOMapper implements Function<News, NewsSceneDTO> {
    @Override
    public NewsSceneDTO apply(@NotNull News news) {
        return NewsSceneDTO.builder()
                .id(news.getId())
                .name(news.getName())
                .description(news.getDescription())
                .createdAt(news.getCreatedAt())
                .updatedAt(news.getUpdatedAt())
                .isDeleted(news.isDeleted())
                .deletedAt(news.getDeletedAt())
                .lastMessage(news.getMessages().stream()
                        .filter(message -> !message.isDeleted())
                        .max(Comparator.comparing(NewsMessages::getCreatedAt))
                        .map(NewsMessages::getMessage)
                        .orElse(null))
                .build();
    }

    public NewsSceneDTO apply(@NotNull News news, int count) {
        return NewsSceneDTO.builder()
                .id(news.getId())
                .name(news.getName())
                .description(news.getDescription())
                .createdAt(news.getCreatedAt())
                .updatedAt(news.getUpdatedAt())
                .isDeleted(news.isDeleted())
                .deletedAt(news.getDeletedAt())
                .lastMessage(news.getMessages().stream()
                        .filter(message -> !message.isDeleted())
                        .max(Comparator.comparing(NewsMessages::getCreatedAt))
                        .map(NewsMessages::getMessage)
                        .orElse(null)
                )
                .lastMessageCreatedAt(news.getMessages().stream()
                        .max(Comparator.comparing(NewsMessages::getCreatedAt))
                        .map(NewsMessages::getCreatedAt).orElse(null)
                )
                .messageCount(news.getMessages().size())
                .unreadCount(count)
                .build();
    }
}
