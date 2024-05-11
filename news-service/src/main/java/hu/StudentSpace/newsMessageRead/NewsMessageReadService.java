package hu.StudentSpace.newsMessageRead;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewsMessageReadService {
    private final NewsMessageReadRepository newsMessageReadRepository;

    public NewsMessageRead save(NewsMessageRead newsMessageRead) {
        return newsMessageReadRepository.save(newsMessageRead);
    }

    public NewsMessageRead findByNewsMessageIdAndUserId(UUID newsMessageId, String userId) {
        return newsMessageReadRepository.findByMessageIdAndUserId(newsMessageId, userId);
    }

    public int getUnreadCount(UUID newsId, String userId) {
        return newsMessageReadRepository.countByMessageIdAndUserId(newsId, userId);
    }
}
