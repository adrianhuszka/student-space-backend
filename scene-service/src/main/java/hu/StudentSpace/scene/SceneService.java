package hu.StudentSpace.scene;

import hu.StudentSpace.exception.AccessDeniedException;
import hu.StudentSpace.exception.ConflictException;
import hu.StudentSpace.exception.ResourceNotFoundException;
import hu.StudentSpace.keycloakHooks.GroupEntityHookRepository;
import hu.StudentSpace.keycloakHooks.UserEntityHookRepository;
import hu.StudentSpace.sceneGroupMembership.SceneGroupMembership;
import hu.StudentSpace.sceneUserMenbership.SceneUserMembership;
import hu.StudentSpace.utils.JwtDecoder;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
    private final JwtDecoder jwtDecoder;
    private final WebClient.Builder webClientBuilder;

    private List<String> findUserGroupIds(@NotNull final String userId) {
        return webClientBuilder.build()
                .get()
                .uri("http://administration-service/api/v1/administration/groups/user/" + userId)
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
    }

    public List<Scene> listAllUserJoinedScenes(@NotNull final String token) {
        final var userId = jwtDecoder.decode(token).sub();
        final var scenes = new ArrayList<>(sceneRepository.findAllBySceneUserMembership(userId));

        List<String> groupIds = findUserGroupIds(userId);

        if (groupIds != null) {
            for (String groupId : groupIds) {
                scenes.addAll(sceneRepository.findAllBySceneGroupMembership(userId, groupId));
            }
        }

        return scenes;
    }

    public List<Scene> listAllUserJoinedScenesArchived(@NotNull final String token) {
        final var userId = jwtDecoder.decode(token).sub();
        final var scenes = new ArrayList<>(sceneRepository.findAllBySceneUserMembershipArchived(userId));

        List<String> groupIds = findUserGroupIds(userId);

        if (groupIds != null) {
            for (String groupId : groupIds) {
                scenes.addAll(sceneRepository.findAllBySceneGroupMembershipArchived(userId, groupId));
            }
        }

        return scenes;
    }

    public void createScene(@NotNull final SceneRequest sceneRequest, @NotNull final String token) {
        final var newScene = Scene.builder()
                .name(sceneRequest.name())
                .description(sceneRequest.description())
                .ownerId(jwtDecoder.decode(token).sub())
                .build();

        sceneRepository.save(newScene);
    }

    public void update(@NotNull final SceneRequest sceneRequest, @NotNull final String token) {
        final var scene = sceneRepository.findById(UUID.fromString(sceneRequest.id()))
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        if (!scene.getOwnerId().equals(jwtDecoder.decode(token).sub())) {
            throw new AccessDeniedException("You are not the owner of this scene");
        }

        scene.setName(sceneRequest.name());
        scene.setDescription(sceneRequest.description());

        sceneRepository.save(scene);
    }

    public void joinByUser(@NotNull final String sceneId, @NotNull final String userId, String role, @NotNull final String token) {
        final var scene = sceneRepository.findById(UUID.fromString(sceneId))
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        if (!scene.getOwnerId().equals(jwtDecoder.decode(token).sub())) {
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

        if (!scene.getOwnerId().equals(jwtDecoder.decode(token).sub())) {
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

        if (!scene.getOwnerId().equals(jwtDecoder.decode(token).sub())) {
            throw new AccessDeniedException("You are not the owner of this scene");
        }

        scene.getSceneUserMembership().removeIf(sceneUserMembership -> sceneUserMembership.getUser().getId().equals(userId));

        sceneRepository.save(scene);
    }

    public void removeGroup(@NotNull final String sceneId, @NotNull final String groupId, @NotNull final String token) {
        final var scene = sceneRepository.findById(UUID.fromString(sceneId))
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        if (!scene.getOwnerId().equals(jwtDecoder.decode(token).sub())) {
            throw new AccessDeniedException("You are not the owner of this scene");
        }

        scene.getSceneGroupMembership().removeIf(sceneGroupMembership -> sceneGroupMembership.getGroup().getId().equals(groupId));

        sceneRepository.save(scene);
    }

    public void archive(@NotNull final String sceneId, @NotNull final String token) {
        final var scene = sceneRepository.findById(UUID.fromString(sceneId))
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        if (!scene.getOwnerId().equals(jwtDecoder.decode(token).sub())) {
            throw new AccessDeniedException("You are not the owner of this scene");
        }

        scene.setArchived(true);
        scene.setArchivedAt(new Timestamp(System.currentTimeMillis()));

        sceneRepository.save(scene);
    }

    public void delete(@NotNull final String sceneId, @NotNull final String token) {
        final var scene = sceneRepository.findById(UUID.fromString(sceneId))
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        if (!scene.getOwnerId().equals(jwtDecoder.decode(token).sub())) {
            throw new AccessDeniedException("You are not the owner of this scene");
        }

        scene.setDeleted(true);
        scene.setDeletedAt(new Timestamp(System.currentTimeMillis()));

        sceneRepository.save(scene);
    }
}
