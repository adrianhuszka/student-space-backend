package hu.StudentSpace.forumLikesMessage;

import hu.StudentSpace.exception.AccessDeniedException;
import hu.StudentSpace.exception.ResourceNotFoundException;
import hu.StudentSpace.forumMessages.ForumMessagesRepository;
import hu.StudentSpace.utils.JwtDecoder;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForumLikesMessageService {
    private final JwtDecoder jwtDecoder;
    private final ForumMessagesRepository forumMessagesRepository;
    private final ForumLikesMessageRepository forumLikesMessageRepository;

    public void addLike(@NotNull final ForumLikesMessageRequest request) {
        final var forumMessage = forumMessagesRepository.findById(UUID.fromString(request.forumMessageId())).orElseThrow(
                () -> new ResourceNotFoundException("Forum message not found with id: " + request.forumMessageId())
        );

        final var forumLikesMessage = forumLikesMessageRepository.save(
                ForumLikesMessage
                        .builder()
                        .forumMessage(forumMessage)
                        .userId(request.userId())
                        .liked(LikedEnum.fromBoolean(request.isLike()))
                        .build()
        );

        forumLikesMessageRepository.save(forumLikesMessage);

        if (forumMessage.getLikes() == null)
            forumMessage.setLikes(new ArrayList<>());

        forumMessage.getLikes().add(forumLikesMessage);

        forumMessagesRepository.save(forumMessage);
    }

    public void modifyLike(@NotNull final ForumLikesMessageRequest request, String token) {
        final var forumLikesMessage = forumLikesMessageRepository.findById(UUID.fromString(request.forumMessageId())).orElseThrow(
                () -> new ResourceNotFoundException("Like not found with id: " + request.forumMessageId())
        );

        if (!forumLikesMessage.getUserId().equals(jwtDecoder.decode(token).getSub()))
            throw new AccessDeniedException("You can't modify this like");

        forumLikesMessage.setLiked(LikedEnum.fromBoolean(request.isLike()));

        forumLikesMessageRepository.save(forumLikesMessage);
    }

    public void removeLike(String likeId, String token) {
        final var forumLikesMessage = forumLikesMessageRepository.findById(UUID.fromString(likeId)).orElseThrow(
                () -> new ResourceNotFoundException("Like not found with id: " + likeId)
        );

        if (!forumLikesMessage.getUserId().equals(jwtDecoder.decode(token).getSub()))
            throw new AccessDeniedException("You can't remove this like");

        final var forumMessage = forumMessagesRepository.findById(forumLikesMessage.getForumMessage().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Forum message not found with id: " + forumLikesMessage.getForumMessage().getId())
        );

        forumMessage.getLikes().remove(forumLikesMessage);

        forumMessagesRepository.save(forumMessage);

        forumLikesMessageRepository.delete(forumLikesMessage);
    }
}
