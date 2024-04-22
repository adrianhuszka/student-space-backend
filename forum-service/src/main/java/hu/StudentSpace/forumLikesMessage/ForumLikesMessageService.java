package hu.StudentSpace.forumLikesMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForumLikesMessageService {
    private final ForumLikesMessageRepository forumLikesMessageRepository;
}
