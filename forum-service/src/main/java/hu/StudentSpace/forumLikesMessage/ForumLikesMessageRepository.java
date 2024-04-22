package hu.StudentSpace.forumLikesMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ForumLikesMessageRepository extends JpaRepository<ForumLikesMessage, UUID> {
}
