package hu.StudentSpace.news;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news/newss")
public class NewsController {
    private final NewsService newsService;

    @GetMapping("/listByScene/{sceneId}/{isOwner}")
    public ResponseEntity<List<NewsSceneDTO>> listNewsBySceneId(@PathVariable String sceneId,
                                                                @PathVariable Boolean isOwner,
                                                                @RequestHeader("Authorization") String token ){
        return ResponseEntity.ok(newsService.listNewsBySceneId(sceneId, isOwner, token));
    }

    @PostMapping
    public ResponseEntity<UUID> createNews(@RequestHeader("Authorization") String token, @RequestBody NewsRequest newsRequest) {
        return ResponseEntity.status(201).body(newsService.createNews(newsRequest, token));
    }

    @PutMapping
    public ResponseEntity<Void> updateNews(@RequestHeader("Authorization") String token, @RequestBody NewsRequest news) {
        newsService.updateNews(news, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{newsId}")
    public ResponseEntity<Void> deleteNews(@RequestHeader("Authorization") String token, @PathVariable String newsId) {
        newsService.deleteNews(newsId, token);
        return ResponseEntity.ok().build();
    }
}
