package hu.StudentSpace.forumMessages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForumMessagesService {
    private final ForumMessagesRepository forumMessagesRepository;
}
