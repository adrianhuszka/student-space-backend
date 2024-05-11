package hu.StudentSpace.newsMessages;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NewsMessagesRepository extends JpaRepository<NewsMessages, UUID> {

    @Query("SELECT f FROM NewsMessages f WHERE f.news.id = :newsId")
    Page<NewsMessages> findAllByNewsId(UUID newsId, Pageable pageable);
}
