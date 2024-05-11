package hu.StudentSpace.newsLikesMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news/message-likes")
public class NewsLikesMessageController {
    private final NewsLikesMessageService newsLikesMessageService;

    @PostMapping
    public ResponseEntity<Void> addLike(@RequestBody NewsLikesMessageRequest request) {
        newsLikesMessageService.addLike(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> modifyLike(@RequestBody NewsLikesMessageRequest request, @RequestHeader("Authorization") String token) {
        newsLikesMessageService.modifyLike(request, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<Void> removeLike(@PathVariable String likeId, @RequestHeader("Authorization") String token) {
        newsLikesMessageService.removeLike(likeId, token);
        return ResponseEntity.ok().build();
    }
}
