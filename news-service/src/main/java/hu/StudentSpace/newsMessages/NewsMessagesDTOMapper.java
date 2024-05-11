package hu.StudentSpace.newsMessages;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class NewsMessagesDTOMapper implements Function<NewsMessages, NewsMessagesDTO> {
    @Override
    public NewsMessagesDTO apply(@NotNull final NewsMessages newsMessages) {
        return new NewsMessagesDTO(
                newsMessages.getId(),
                newsMessages.getMessage(),
                newsMessages.getSenderId(),
                newsMessages.getSenderName(),
                newsMessages.getAnswerTo(),
                newsMessages.getCreatedAt(),
                newsMessages.getUpdatedAt(),
                newsMessages.getLikes()
        );
    }
}
