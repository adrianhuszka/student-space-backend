package hu.StudentSpace.scene;

import hu.StudentSpace.exception.AccessDeniedException;
import hu.StudentSpace.exception.ConflictException;
import hu.StudentSpace.exception.ResourceNotFoundException;
import hu.StudentSpace.keycloakHooks.GroupEntityHookRepository;
import hu.StudentSpace.keycloakHooks.UserEntityHookRepository;
import hu.StudentSpace.sceneGroupMembership.SceneGroupMembership;
import hu.StudentSpace.sceneItems.SceneItemType;
import hu.StudentSpace.sceneItems.SceneItems;
import hu.StudentSpace.sceneItems.SceneReturnItems;
import hu.StudentSpace.sceneUserMenbership.SceneUserMembership;
import hu.StudentSpace.sceneUserMenbership.SceneUserMembershipRepository;
import hu.StudentSpace.utils.JwtDecoder;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SceneService {
    private final SceneRepository sceneRepository;
    private final UserEntityHookRepository userEntityHookRepository;
    private final GroupEntityHookRepository groupEntityHookRepository;
    private final SceneUserMembershipRepository sceneUserMembershipRepository;
    private final JwtDecoder jwtDecoder;
    private final WebClient.Builder webClientBuilder;

    private List<String> findUserGroupIds(@NotNull final String userId, @NotNull final String token) {
        return webClientBuilder.build()
                .get()
                .uri("http://administration-service/api/v1/administration/groups/user/" + userId)
                .header("Authorization", token)
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
    }

    public boolean ownerCheck(String token, String sceneId) {
        final var scene = sceneRepository.findById(UUID.fromString(sceneId))
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));
        return scene.getOwnerId().equals(jwtDecoder.decode(token).getSub());
    }

    public List<Scene> listAllUserJoinedScenes(@NotNull final String token) {
        final var userId = jwtDecoder.decode(token).getSub();
        final var scenes = new ArrayList<>(sceneRepository.findAllBySceneUserMembership(userId));

        List<String> groupIds = findUserGroupIds(userId, token);

        if (groupIds != null) {
            for (String groupId : groupIds) {
                scenes.addAll(sceneRepository.findAllBySceneGroupMembership(userId, groupId));
            }
        }

        scenes.forEach(scene ->
            scene.setItems(getSceneItems(scene.getId(), token))
        );

        return scenes;
    }

    public List<Scene> listAllUserJoinedScenesArchived(@NotNull final String token) {
        final var userId = jwtDecoder.decode(token).getSub();
        final var scenes = new ArrayList<>(sceneRepository.findAllBySceneUserMembershipArchived(userId));

        List<String> groupIds = findUserGroupIds(userId, token);

        if (groupIds != null) {
            for (String groupId : groupIds) {
                scenes.addAll(sceneRepository.findAllBySceneGroupMembershipArchived(userId, groupId));
            }
        }

        scenes.forEach(scene ->
            scene.setItems(getSceneItems(scene.getId(), token))
        );

        return scenes;
    }

    public Scene getSceneById(@NotNull final String sceneId, @NotNull final String token) {
        final var scene = sceneRepository.findById(UUID.fromString(sceneId))
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        scene.setItems(getSceneItems(scene.getId(), token));

        return scene;
    }

    public void createScene(@NotNull final SceneRequest sceneRequest, @NotNull final String token) {
        final var userId = jwtDecoder.decode(token).getSub();

        final var user = userEntityHookRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        final var newScene = Scene.builder()
                .name(sceneRequest.name())
                .description(sceneRequest.description())
                .ownerId(userId)
                .archived(false)
                .deleted(false)
                .build();

        final var savedScene = sceneRepository.save(newScene);

        final var savedMembership = sceneUserMembershipRepository.save(
                SceneUserMembership.builder()
                        .user(user)
                        .scene(savedScene)
                        .role(SceneMembershipRole.OWNER)
                        .build()
        );

        savedScene.setSceneUserMembership(
                List.of(savedMembership)
        );

        sceneRepository.save(savedScene);

        createStarterItems(savedScene.getId().toString(), token);
    }

    public void update(@NotNull final SceneRequest sceneRequest, @NotNull final String token) {
        final var scene = sceneRepository.findById(UUID.fromString(sceneRequest.id()))
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        if (ownerCheck(token, sceneRequest.id())) {
            throw new AccessDeniedException("You are not the owner of this scene");
        }

        scene.setName(sceneRequest.name());
        scene.setDescription(sceneRequest.description());

        sceneRepository.save(scene);
    }

    public void joinByUser(@NotNull final String sceneId, @NotNull final String userId, String role, @NotNull final String token) {
        final var scene = sceneRepository.findById(UUID.fromString(sceneId))
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        if (ownerCheck(token, sceneId)) {
            throw new AccessDeniedException("You are not the owner of this scene");
        }

        if (scene.getSceneUserMembership().stream().anyMatch(sceneUserMembership -> sceneUserMembership.getUser().getId().equals(userId))) {
            throw new ConflictException("User is already a member of this scene");
        }

        final var userEntity = userEntityHookRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        scene.getSceneUserMembership().add(SceneUserMembership.builder()
                .user(userEntity)
                .scene(scene)
                .role(SceneMembershipRole.fromString(role))
                .build());

        sceneRepository.save(scene);
    }

    public void joinByGroup(@NotNull final String sceneId, @NotNull final String groupId, @NotNull final String token) {
        final var scene = sceneRepository.findById(UUID.fromString(sceneId))
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        if (ownerCheck(token, sceneId)) {
            throw new AccessDeniedException("You are not the owner of this scene");
        }

        if (scene.getSceneGroupMembership().stream().anyMatch(sceneGroupMembership -> sceneGroupMembership.getGroup().getId().equals(groupId))) {
            throw new ConflictException("This group is already a member of this scene");
        }

        scene.getSceneGroupMembership().add(SceneGroupMembership.builder()
                .group(groupEntityHookRepository.findById(groupId).orElseThrow(
                        () -> new ResourceNotFoundException("Group not found")
                ))
                .scene(scene)
                .build());

        sceneRepository.save(scene);
    }

    public void removeUser(@NotNull final String sceneId, @NotNull final String userId, @NotNull final String token) {
        final var scene = sceneRepository.findById(UUID.fromString(sceneId))
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        if (ownerCheck(token, sceneId)) {
            throw new AccessDeniedException("You are not the owner of this scene");
        }

        scene.getSceneUserMembership().removeIf(sceneUserMembership -> sceneUserMembership.getUser().getId().equals(userId));

        sceneRepository.save(scene);
    }

    public void removeGroup(@NotNull final String sceneId, @NotNull final String groupId, @NotNull final String token) {
        final var scene = sceneRepository.findById(UUID.fromString(sceneId))
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        if (ownerCheck(token, sceneId)) {
            throw new AccessDeniedException("You are not the owner of this scene");
        }

        scene.getSceneGroupMembership().removeIf(sceneGroupMembership -> sceneGroupMembership.getGroup().getId().equals(groupId));

        sceneRepository.save(scene);
    }

    public void archive(@NotNull final String sceneId, @NotNull final String token) {
        final var scene = sceneRepository.findById(UUID.fromString(sceneId))
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        if (ownerCheck(token, sceneId)) {
            throw new AccessDeniedException("You are not the owner of this scene");
        }

        scene.setArchived(true);
        scene.setArchivedAt(new Timestamp(System.currentTimeMillis()));

        sceneRepository.save(scene);
    }

    public void delete(@NotNull final String sceneId, @NotNull final String token) {
        final var scene = sceneRepository.findById(UUID.fromString(sceneId))
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        if (ownerCheck(token, sceneId)) {
            throw new AccessDeniedException("You are not the owner of this scene");
        }

        scene.setDeleted(true);
        scene.setDeletedAt(new Timestamp(System.currentTimeMillis()));

        sceneRepository.save(scene);
    }

    @NotNull
    private List<SceneReturnItems> getSceneItems(@NotNull final UUID sceneId, final String token) {
        final var forum = webClientBuilder.build()
                .get()
                .uri("http://forum-service/api/v1/forum/forums/listByScene/" + sceneId + "/" + ownerCheck(token, sceneId.toString()))
                .header("Authorization", token)
                .retrieve()
                .bodyToFlux(SceneReturnItems.class)
                .collectList()
                .block();

        if(forum != null)
            forum.forEach(forumItem -> forumItem.setType(SceneItemType.FORUM));

        assert forum != null;
        return forum;
    }

    private void createStarterItems(final String sceneId, final String token) {
        final var scene = sceneRepository.findById(UUID.fromString(sceneId))
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        final var createdForum = webClientBuilder.build()
                .post()
                .uri("http://forum-service/api/v1/forum/forums")
                .header("Authorization", token)
                .bodyValue(new Payload(sceneId, "General", "General discussion"))
                .retrieve()
                .bodyToMono(UUID.class)
                .block();

        final var createdNews = webClientBuilder.build()
                .post()
                .uri("http://news-service/api/v1/news/room")
                .header("Authorization", token)
                .bodyValue(new Payload(sceneId, "General", "General news"))
                .retrieve()
                .bodyToMono(UUID.class)
                .block();

        scene.setDbItems(
                List.of(
                        SceneItems.builder()
                                .id(createdForum)
                                .type(SceneItemType.FORUM)
                                .build(),
                        SceneItems.builder()
                                .id(createdNews)
                                .type(SceneItemType.NEWS)
                                .build()
                )
        );

        sceneRepository.save(scene);
    }

    private record Payload(String sceneId, String name, String description) implements Serializable {
    }
}
