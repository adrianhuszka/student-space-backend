package hu.StudentSpace.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ForumRepository extends JpaRepository<Forum, UUID> {

    @Query("SELECT f FROM Forum f WHERE f.sceneId = :sceneId AND (f.isDeleted = false OR :owner)")
    List<Forum> findAllBySceneIdAndDeletedFalse(UUID sceneId, boolean owner);
}
