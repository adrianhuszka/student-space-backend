package hu.StudentSpace.forumMessageRead;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ForumMessageReadRepository extends JpaRepository<ForumMessageRead, ForumMessageReadId> {
    ForumMessageRead findByMessageIdAndUserId(UUID messageId, String userId);

    @Query("select count(*) " +
            "from ForumMessages fm " +
            "left join ForumMessageRead fmr on fm.id = fmr.messageId " +
            "where fm.forum.id = :forumId and fm.senderId = :userId and fmr.userId is null"
    )
    int countByMessageIdAndUserId(UUID forumId, String userId);
}
