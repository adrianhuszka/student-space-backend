package hu.StudentSpace.news;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NewsRepository extends JpaRepository<News, UUID> {

    @Query("SELECT n FROM News n WHERE n.sceneId = :sceneId AND (n.isDeleted = false OR :owner = true)")
    List<News> findAllBySceneIdAndDeletedFalse(UUID sceneId, boolean owner);
}
