package hu.StudentSpace.newsMessages;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news/messages")
public class NewsMessagesController {
    private final NewsMessagesService newsMessagesService;

    @GetMapping
    public ResponseEntity<Page<NewsMessages>> findAllByNewsId(@RequestParam final String newsId,
                                                                @RequestParam final int page,
                                                                @RequestParam final int size,
                                                                @RequestParam final String sort,
                                                                @RequestParam final String direction) {
        return ResponseEntity.ok(newsMessagesService.findAllByNewsId(newsId, page, size, sort, direction));
    }

    @PostMapping
    public ResponseEntity<Void> createNewsMessage(@RequestBody final NewsMessagesRequest newsMessages) {
        newsMessagesService.createNewsMessage(newsMessages);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateNewsMessage(@RequestBody final NewsMessagesRequest request, @RequestHeader("Authorization") final String token) {
        newsMessagesService.updateNewsMessage(request, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteNewsMessage(@PathVariable final String messageId, @RequestHeader("Authorization") final String token) {
        newsMessagesService.deleteNewsMessage(messageId, token);
        return ResponseEntity.ok().build();
    }
}
