package hu.StudentSpace.forumMessages;

import hu.StudentSpace.exception.AccessDeniedException;
import hu.StudentSpace.forum.ForumRepository;
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
public class ForumMessagesService {
    private final JwtDecoder jwtDecoder;
    private final WebClient.Builder webClientBuilder;
    private final ForumRepository forumRepository;
    private final ForumMessagesRepository forumMessagesRepository;

    public Page<ForumMessages> findAllByForumId(final String forumId, final int page, final int size, final String sort, final String direction) {
        final var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));

        return forumMessagesRepository.findAllByForumId(UUID.fromString(forumId), pageable);
    }

    public void createForumMessage(@NotNull final ForumMessagesRequest forumMessages) {
        final var forum = forumRepository.findById(UUID.fromString(forumMessages.forumId()))
                .orElseThrow(() -> new RuntimeException("Forum not found"));

        final var answerTo = forumMessages.answerToId() != null
                ? forumMessagesRepository.findById(UUID.fromString(forumMessages.answerToId())).orElseThrow(() -> new RuntimeException("Answer to not found"))
                : null;

        final var newForumMessage = ForumMessages.builder()
                .forum(forum)
                .message(forumMessages.message())
                .senderId(forumMessages.senderId())
                .answerTo(answerTo)
                .build();

        final var savedMessage = forumMessagesRepository.save(newForumMessage);

        if (forum.getMessages() == null) {
            forum.setMessages(new ArrayList<>());
        }

        forum.getMessages().add(savedMessage);
    }

    public void updateForumMessage(@NotNull final ForumMessagesRequest request, String token) {
        final var forumMessage = forumMessagesRepository.findById(UUID.fromString(request.id()))
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!forumMessage.getSenderId().equals(jwtDecoder.decode(token).getSub()) || !ownerCheck(token, forumMessage.getForum().getSceneId().toString())) {
            throw new AccessDeniedException("You are not the owner of this message");
        }

        forumMessage.setMessage(request.message());

        forumMessagesRepository.save(forumMessage);
    }

    public void deleteForumMessage(final String messageId, String token) {
        final var forumMessage = forumMessagesRepository.findById(UUID.fromString(messageId))
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!forumMessage.getSenderId().equals(jwtDecoder.decode(token).getSub()) || !ownerCheck(token, forumMessage.getForum().getSceneId().toString())) {
            throw new AccessDeniedException("You are not the owner of this message");
        }

        forumMessage.setDeleted(true);
        forumMessage.setDeletedAt(new Timestamp(System.currentTimeMillis()));

        forumMessagesRepository.save(forumMessage);
    }

    private boolean ownerCheck(String token, String sceneId) {
        final var ownerCheck = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/v1/scenes/ownerCheck/" + sceneId)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        return Boolean.TRUE.equals(ownerCheck);
    }
}
