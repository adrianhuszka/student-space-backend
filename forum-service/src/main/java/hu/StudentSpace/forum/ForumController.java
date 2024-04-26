package hu.StudentSpace.forum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/forum/forums")
public class ForumController {
    private final ForumService forumService;

    @GetMapping("/listByScene/{sceneId}/{isOwner}")
    public ResponseEntity<List<ForumSceneDTO>> listForumsBySceneId(@PathVariable String sceneId, @PathVariable Boolean isOwner) {
        return ResponseEntity.ok(forumService.listForumsBySceneId(sceneId, isOwner));
    }

    @PostMapping
    public ResponseEntity<Void> createForum(@RequestHeader("Authorization") String token, @RequestBody ForumRequest forum) {
        forumService.createForum(forum, token);
        return ResponseEntity.status(201).build();
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
