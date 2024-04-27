package hu.StudentSpace.forum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/forum/forums")
public class ForumController {
    private final ForumService forumService;

    @GetMapping("/listByScene/{sceneId}/{isOwner}")
    public ResponseEntity<List<ForumSceneDTO>> listForumsBySceneId(@PathVariable String sceneId,
                                                                   @PathVariable Boolean isOwner,
                                                                   @RequestHeader("Authorization") String token ){
        return ResponseEntity.ok(forumService.listForumsBySceneId(sceneId, isOwner, token));
    }

    @PostMapping
    public ResponseEntity<UUID> createForum(@RequestHeader("Authorization") String token, @RequestBody ForumRequest forumRequest) {
        return ResponseEntity.status(201).body(forumService.createForum(forumRequest, token));
    }

    @PutMapping
    public ResponseEntity<Void> updateForum(@RequestHeader("Authorization") String token, @RequestBody ForumRequest forum) {
        forumService.updateForum(forum, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{forumId}")
    public ResponseEntity<Void> deleteForum(@RequestHeader("Authorization") String token, @PathVariable String forumId) {
        forumService.deleteForum(forumId, token);
        return ResponseEntity.ok().build();
    }
}
