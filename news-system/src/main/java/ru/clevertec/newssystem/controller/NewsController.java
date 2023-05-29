package ru.clevertec.newssystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.newssystem.dto.news.NewsDTO;
import ru.clevertec.newssystem.dto.news.NewsRequest;
import ru.clevertec.newssystem.dto.news.NewsResponse;
import ru.clevertec.newssystem.service.NewsService;

import java.net.URI;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<Page<NewsDTO>> getNews(Pageable pageable) {
        Page<NewsDTO> news = newsService.findAll(pageable);
        return ResponseEntity.ok(news);
    }

    @PostMapping
    public ResponseEntity<NewsDTO> createNews(@Valid @RequestBody NewsRequest newsRequest) {
        NewsDTO newsResponse = newsService.create(newsRequest);
        return ResponseEntity
                .created(URI.create("/api/v1/news/" + newsResponse.getId()))
                .body(newsResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> getNewsById(
            @PathVariable Integer id,
            Pageable pageable) {
        NewsResponse news = newsService.find(id, pageable);
        return ResponseEntity.ok(news);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NewsDTO> updateNews(
            @PathVariable Integer id,
            @Valid @RequestBody NewsRequest request
    ) {
        NewsDTO updatedNews = newsService.update(id, request);
        return ResponseEntity.ok(updatedNews);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Integer id) {
        newsService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
