package hu.StudentSpace.forumMessages;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/forum/messages")
public class ForumMessagesController {
    private final ForumMessagesService forumMessagesService;

    @GetMapping
    public ResponseEntity<Page<ForumMessages>> findAllByForumId(@RequestParam final String forumId,
                                                                @RequestParam final int page,
                                                                @RequestParam final int size,
                                                                @RequestParam final String sort,
                                                                @RequestParam final String direction) {
        return ResponseEntity.ok(forumMessagesService.findAllByForumId(forumId, page, size, sort, direction));
    }

    @PostMapping
    public ResponseEntity<Void> createForumMessage(@RequestBody final ForumMessagesRequest forumMessages, @RequestHeader("Authorization") final String token) {
        forumMessagesService.createForumMessage(forumMessages, token);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateForumMessage(@RequestBody final ForumMessagesRequest request, @RequestHeader("Authorization") final String token) {
        forumMessagesService.updateForumMessage(request, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteForumMessage(@PathVariable final String messageId, @RequestHeader("Authorization") final String token) {
        forumMessagesService.deleteForumMessage(messageId, token);
        return ResponseEntity.ok().build();
    }
}
