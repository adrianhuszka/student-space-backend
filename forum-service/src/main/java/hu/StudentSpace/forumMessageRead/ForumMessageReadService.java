package hu.StudentSpace.forumMessageRead;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForumMessageReadService {
    private final ForumMessageReadRepository forumMessageReadRepository;

    public ForumMessageRead save(ForumMessageRead forumMessageRead) {
        return forumMessageReadRepository.save(forumMessageRead);
    }

    public ForumMessageRead findByForumMessageIdAndUserId(UUID forumMessageId, String userId) {
        return forumMessageReadRepository.findByMessageIdAndUserId(forumMessageId, userId);
    }

    public int getUnreadCount(UUID forumId, String userId) {
        return forumMessageReadRepository.countByMessageIdAndUserId(forumId, userId);
    }
}
