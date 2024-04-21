package hu.StudentSpace.scene;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SceneRepository extends JpaRepository<Scene, UUID> {

    @Query("SELECT s FROM Scene s JOIN s.sceneUserMembership sm WHERE sm.user.id = :userId AND (s.deleted = false OR s.ownerId = :userId) AND s.archived = false")
    List<Scene> findAllBySceneUserMembership(@Param("userId") String userId);

    @Query("SELECT s FROM Scene s JOIN s.sceneUserMembership sm WHERE sm.user.id = :userId AND (s.deleted = false OR s.ownerId = :userId) AND s.archived = true")
    List<Scene> findAllBySceneUserMembershipArchived(@Param("userId") String userId);

    @Query("SELECT s FROM Scene s JOIN s.sceneGroupMembership sm WHERE sm.group.id = :groupId AND (s.deleted = false OR s.ownerId = :userId) AND s.archived = false")
    List<Scene> findAllBySceneGroupMembership(@Param("userId") String userId, @Param("groupId") String groupId);

    @Query("SELECT s FROM Scene s JOIN s.sceneGroupMembership sm WHERE sm.group.id = :groupId AND (s.deleted = false OR s.ownerId = :userId) AND s.archived = true")
    List<Scene> findAllBySceneGroupMembershipArchived(@Param("userId") String userId, @Param("groupId") String groupId);
}
