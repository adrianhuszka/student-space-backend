package hu.StudentSpace.newsLikesMessage;

import hu.StudentSpace.exception.AccessDeniedException;
import hu.StudentSpace.exception.ResourceNotFoundException;
import hu.StudentSpace.newsMessages.NewsMessagesRepository;
import hu.StudentSpace.utils.JwtDecoder;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsLikesMessageService {
    private final JwtDecoder jwtDecoder;
    private final NewsMessagesRepository newsMessagesRepository;
    private final NewsLikesMessageRepository newsLikesMessageRepository;

    public void addLike(@NotNull final NewsLikesMessageRequest request) {
        final var newsMessage = newsMessagesRepository.findById(UUID.fromString(request.newsMessageId())).orElseThrow(
                () -> new ResourceNotFoundException("News message not found with id: " + request.newsMessageId())
        );

        final var newsLikesMessage = newsLikesMessageRepository.save(
                NewsLikesMessage
                        .builder()
                        .newsMessage(newsMessage)
                        .userId(request.userId())
                        .liked(LikedEnum.fromBoolean(request.isLike()))
                        .build()
        );

        newsLikesMessageRepository.save(newsLikesMessage);

        if (newsMessage.getLikes() == null)
            newsMessage.setLikes(new ArrayList<>());

        newsMessage.getLikes().add(newsLikesMessage);

        newsMessagesRepository.save(newsMessage);
    }

    public void modifyLike(@NotNull final NewsLikesMessageRequest request, String token) {
        final var newsLikesMessage = newsLikesMessageRepository.findById(UUID.fromString(request.newsMessageId())).orElseThrow(
                () -> new ResourceNotFoundException("Like not found with id: " + request.newsMessageId())
        );

        if (!newsLikesMessage.getUserId().equals(jwtDecoder.decode(token).getSub()))
            throw new AccessDeniedException("You can't modify this like");

        newsLikesMessage.setLiked(LikedEnum.fromBoolean(request.isLike()));

        newsLikesMessageRepository.save(newsLikesMessage);
    }

    public void removeLike(String likeId, String token) {
        final var newsLikesMessage = newsLikesMessageRepository.findById(UUID.fromString(likeId)).orElseThrow(
                () -> new ResourceNotFoundException("Like not found with id: " + likeId)
        );

        if (!newsLikesMessage.getUserId().equals(jwtDecoder.decode(token).getSub()))
            throw new AccessDeniedException("You can't remove this like");

        final var newsMessage = newsMessagesRepository.findById(newsLikesMessage.getNewsMessage().getId()).orElseThrow(
                () -> new ResourceNotFoundException("News message not found with id: " + newsLikesMessage.getNewsMessage().getId())
        );

        newsMessage.getLikes().remove(newsLikesMessage);

        newsMessagesRepository.save(newsMessage);

        newsLikesMessageRepository.delete(newsLikesMessage);
    }
}
