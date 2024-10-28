package hu.StudentSpace.newsMessages;

import hu.StudentSpace.exception.AccessDeniedException;
import hu.StudentSpace.news.NewsRepository;
import hu.StudentSpace.utils.JwtDecoder;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsMessagesService {
    private final JwtDecoder jwtDecoder;
    private final WebClient.Builder webClientBuilder;
    private final NewsRepository newsRepository;
    private final NewsMessagesRepository newsMessagesRepository;

    public Page<NewsMessages> findAllByNewsId(final String newsId, final int page, final int size, final String sort, final String direction) {
        final var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));

        return newsMessagesRepository.findAllByNewsId(UUID.fromString(newsId), pageable);
    }

    public void createNewsMessage(@NotNull final NewsMessagesRequest newsMessages, String token) {
        final var userId = jwtDecoder.decode(token).getSub();
        final var news = newsRepository.findById(UUID.fromString(newsMessages.newsId()))
                .orElseThrow(() -> new RuntimeException("News not found"));

        final var answerTo = newsMessages.answerToId() != null
                ? newsMessagesRepository.findById(UUID.fromString(newsMessages.answerToId())).orElseThrow(() -> new RuntimeException("Answer to not found"))
                : null;

        final var newNewsMessage = NewsMessages.builder()
                .news(news)
                .message(newsMessages.message())
                .senderId(userId)
                .answerTo(answerTo)
                .build();

        final var savedMessage = newsMessagesRepository.save(newNewsMessage);

        if (news.getMessages() == null) {
            news.setMessages(new ArrayList<>());
        }

        news.getMessages().add(savedMessage);
    }

    public void updateNewsMessage(@NotNull final NewsMessagesRequest request, String token) {
        final var newsMessage = newsMessagesRepository.findById(UUID.fromString(request.id()))
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!newsMessage.getSenderId().equals(jwtDecoder.decode(token).getSub()) || !ownerCheck(token, newsMessage.getNews().getSceneId().toString())) {
            throw new AccessDeniedException("You are not the owner of this message");
        }

        newsMessage.setMessage(request.message());

        newsMessagesRepository.save(newsMessage);
    }

    public void deleteNewsMessage(final String messageId, String token) {
        final var newsMessage = newsMessagesRepository.findById(UUID.fromString(messageId))
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!newsMessage.getSenderId().equals(jwtDecoder.decode(token).getSub()) || !ownerCheck(token, newsMessage.getNews().getSceneId().toString())) {
            throw new AccessDeniedException("You are not the owner of this message");
        }

        newsMessage.setDeleted(true);
        newsMessage.setDeletedAt(new Timestamp(System.currentTimeMillis()));

        newsMessagesRepository.save(newsMessage);
    }

    private boolean ownerCheck(String token, String sceneId) {
        final var ownerCheck = webClientBuilder.build()
                .get()
                .uri("http://scene-service/api/v1/scenes/ownerCheck/" + sceneId)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        return Boolean.TRUE.equals(ownerCheck);
    }
}
