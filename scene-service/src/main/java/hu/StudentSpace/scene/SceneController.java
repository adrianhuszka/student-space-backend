package hu.StudentSpace.scene;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scenes")
public class SceneController {
    private final SceneService sceneService;

    @GetMapping
    @Retry(name = "admin-service")
    @TimeLimiter(name = "admin-service")
    @CircuitBreaker(name = "admin-service", fallbackMethod = "ListAllScenesFallback")
    public CompletableFuture<ResponseEntity<List<Scene>>> listAllUserJoinedScenes(@RequestHeader("Authorization") String token) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(sceneService.listAllUserJoinedScenes(token)));
    }

    @GetMapping("/archived")
    @Retry(name = "admin-service")
    @TimeLimiter(name = "admin-service")
    @CircuitBreaker(name = "admin-service", fallbackMethod = "ListAllScenesFallback")
    public CompletableFuture<ResponseEntity<List<Scene>>> listAllUserJoinedScenesArchived(@RequestHeader("Authorization") String token) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(sceneService.listAllUserJoinedScenesArchived(token)));
    }

    public CompletableFuture<ResponseEntity<String>> ListAllScenesFallback() {
        return CompletableFuture.completedFuture(ResponseEntity.status(404).body("Admin service is not available at the moment. Please try again later. (Scene service fallback method)"));
    }

    @PostMapping
    public ResponseEntity<Scene> createScene(@RequestHeader("Authorization") String token, @RequestBody SceneRequest scene) {
        sceneService.createScene(scene, token);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Scene> updateScene(@RequestHeader("Authorization") String token, @RequestBody SceneRequest scene) {
        sceneService.update(scene, token);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/joinUser/{sceneId}/{userId}/{role}")
    public ResponseEntity<Scene> joinUser(@RequestHeader("Authorization") String token, @PathVariable String userId, @PathVariable String sceneId, @PathVariable String role) {
        sceneService.joinByUser(sceneId, userId, role, token);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/joinGroup/{sceneId}/{groupId}")
    public ResponseEntity<Scene> joinGroup(@RequestHeader("Authorization") String token, @PathVariable String groupId, @PathVariable String sceneId) {
        sceneService.joinByGroup(sceneId, groupId, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/removeUser/{sceneId}/{userId}")
    public ResponseEntity<Scene> removeUser(@RequestHeader("Authorization") String token, @PathVariable String userId, @PathVariable String sceneId) {
        sceneService.removeUser(sceneId, userId, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/removeGroup/{sceneId}/{groupId}")
    public ResponseEntity<Scene> removeGroup(@RequestHeader("Authorization") String token, @PathVariable String groupId, @PathVariable String sceneId) {
        sceneService.removeGroup(sceneId, groupId, token);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/archive/{sceneId}")
    public ResponseEntity<Scene> archiveScene(@PathVariable String sceneId, @RequestHeader("Authorization") String token) {
        sceneService.archive(sceneId, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{sceneId}")
    public ResponseEntity<Scene> deleteScene(@PathVariable String sceneId, @RequestHeader("Authorization") String token) {
        sceneService.delete(sceneId, token);
        return ResponseEntity.ok().build();
    }
}
