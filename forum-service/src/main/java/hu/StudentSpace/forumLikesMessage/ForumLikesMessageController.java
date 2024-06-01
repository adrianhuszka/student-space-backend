package hu.StudentSpace.forumLikesMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/forum/message-likes")
public class ForumLikesMessageController {
    private final ForumLikesMessageService forumLikesMessageService;

    @PostMapping
    public ResponseEntity<Void> addLike(@RequestBody ForumLikesMessageRequest request, @RequestHeader("Authorization") String token) {
        forumLikesMessageService.addLike(request, token);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> modifyLike(@RequestBody ForumLikesMessageRequest request, @RequestHeader("Authorization") String token) {
        forumLikesMessageService.modifyLike(request, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<Void> removeLike(@PathVariable String likeId, @RequestHeader("Authorization") String token) {
        forumLikesMessageService.removeLike(likeId, token);
        return ResponseEntity.ok().build();
    }
}
