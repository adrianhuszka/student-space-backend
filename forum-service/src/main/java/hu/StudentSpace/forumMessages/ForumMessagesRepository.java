package hu.StudentSpace.forumMessages;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ForumMessagesRepository extends JpaRepository<ForumMessages, UUID> {

    @Query("SELECT f FROM ForumMessages f WHERE f.forum.id = :forumId AND f.isDeleted = false")
    Page<ForumMessages> findAllByForumId(UUID forumId, Pageable pageable);
}
