package hu.StudentSpace.newsMessageRead;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NewsMessageReadRepository extends JpaRepository<NewsMessageRead, NewsMessageReadId> {
    NewsMessageRead findByMessageIdAndUserId(UUID messageId, String userId);

    @Query("select count(*) " +
            "from NewsMessages fm " +
            "left join NewsMessageRead fmr on fm.id = fmr.messageId " +
            "where fm.news.id = :newsId and fm.senderId = :userId and fmr.userId is null"
    )
    int countByMessageIdAndUserId(UUID newsId, String userId);
}
