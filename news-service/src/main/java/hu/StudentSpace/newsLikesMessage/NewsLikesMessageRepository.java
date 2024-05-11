package hu.StudentSpace.newsLikesMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NewsLikesMessageRepository extends JpaRepository<NewsLikesMessage, UUID> {
}
