package hu.StudentSpace.news;

import hu.StudentSpace.exception.AccessDeniedException;
import hu.StudentSpace.exception.ResourceNotFoundException;
import hu.StudentSpace.newsMessageRead.newsMessageReadService;
import hu.StudentSpace.utils.JwtDecoder;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsMessageReadService newsMessageReadService;
    private final NewsRepository newsRepository;
    private final NewsSceneDTOMapper newsSceneDTOMapper;
    private final WebClient.Builder webClientBuilder;
    private final JwtDecoder jwtDecoder;

    public List<NewsSceneDTO> listnewssBySceneId(@NotNull final String sceneId, Boolean isOwner, String token) {
        final var userId = jwtDecoder.decode(token).getSub();
        final var list = newsRepository
                .findAllBySceneIdAndDeletedFalse(UUID.fromString(sceneId), isOwner);

        AtomicInteger count = new AtomicInteger(0);

        list.forEach(
                news -> {
                    if(news.getMessages() != null)
                        news.getMessages().forEach(
                            message -> count.addAndGet(newsMessageReadService.getUnreadCount(message.getId(), userId))
                        );
                }
        );

        return list.stream()
                .map(news -> newsSceneDTOMapper.apply(news, count.get()))
                .toList();
    }

    public UUID createnews(@NotNull final newsRequest news, String token) {
        ownerCheck(token, news.sceneId());

        return newsRepository.save(news.builder()
                .name(news.name())
                .sceneId(UUID.fromString(news.sceneId()))
                .description(news.description())
                .build()).getId();
    }

    public void updatenews(@NotNull final newsRequest news, String token) {
        ownerCheck(token, news.sceneId());

        newsRepository.save(news.builder()
                .id(UUID.fromString(news.id()))
                .name(news.name())
                .description(news.description())
                .sceneId(UUID.fromString(news.sceneId()))
                .build());
    }

    public void deletenews(@NotNull final String newsId, String token) {
        final var news = newsRepository.findById(UUID.fromString(newsId)).orElseThrow(
                () -> new ResourceNotFoundException("news not found!")
        );

        ownerCheck(token, news.getSceneId().toString());

        news.setDeleted(true);
        news.setDeletedAt(new Timestamp(System.currentTimeMillis()));

        newsRepository.save(news);
    }

    private void ownerCheck(String token, String sceneId) {
        final var ownerCheck = webClientBuilder.build()
                .get()
                .uri("http://scene-service:8080/api/v1/scenes/ownerCheck/" + sceneId)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.FALSE.equals(ownerCheck)) {
            throw new AccessDeniedException("You are not the owner of the scene!");
        }
    }
}
