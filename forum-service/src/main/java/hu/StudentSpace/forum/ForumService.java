package hu.StudentSpace.forum;

import hu.StudentSpace.exception.AccessDeniedException;
import hu.StudentSpace.exception.ResourceNotFoundException;
import hu.StudentSpace.forumMessageRead.ForumMessageReadService;
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
public class ForumService {
    private final ForumMessageReadService forumMessageReadService;
    private final ForumRepository forumRepository;
    private final ForumSceneDTOMapper forumSceneDTOMapper;
    private final WebClient.Builder webClientBuilder;
    private final JwtDecoder jwtDecoder;

    public List<ForumSceneDTO> listForumsBySceneId(@NotNull final String sceneId, Boolean isOwner, String token) {
        final var userId = jwtDecoder.decode(token).getSub();
        final var list = forumRepository
                .findAllBySceneIdAndDeletedFalse(UUID.fromString(sceneId), isOwner);

        AtomicInteger count = new AtomicInteger(0);

        list.forEach(
                forum -> {
                    if(forum.getMessages() != null)
                        forum.getMessages().forEach(
                            message -> count.addAndGet(forumMessageReadService.getUnreadCount(message.getId(), userId))
                        );
                }
        );

        return list.stream()
                .map(forum -> forumSceneDTOMapper.apply(forum, count.get()))
                .toList();
    }

    public UUID createForum(@NotNull final ForumRequest forum, String token) {
        ownerCheck(token, forum.sceneId());

        return forumRepository.save(Forum.builder()
                .name(forum.name())
                .sceneId(UUID.fromString(forum.sceneId()))
                .description(forum.description())
                .build()).getId();
    }

    public void updateForum(@NotNull final ForumRequest forum, String token) {
        ownerCheck(token, forum.sceneId());

        forumRepository.save(Forum.builder()
                .id(UUID.fromString(forum.id()))
                .name(forum.name())
                .description(forum.description())
                .sceneId(UUID.fromString(forum.sceneId()))
                .build());
    }

    public void deleteForum(@NotNull final String forumId, String token) {
        final var forum = forumRepository.findById(UUID.fromString(forumId)).orElseThrow(
                () -> new ResourceNotFoundException("Forum not found!")
        );

        ownerCheck(token, forum.getSceneId().toString());

        forum.setDeleted(true);
        forum.setDeletedAt(new Timestamp(System.currentTimeMillis()));

        forumRepository.save(forum);
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
