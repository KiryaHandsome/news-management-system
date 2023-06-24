package ru.clevertec.news.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.news.dto.news.NewsDTO;
import ru.clevertec.news.dto.news.NewsRequest;
import ru.clevertec.news.dto.news.NewsResponse;
import ru.clevertec.news.service.NewsService;

import java.net.URI;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<Page<NewsDTO>> getNews(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String title,
            Pageable pageable
    ) {
        Page<NewsDTO> news = newsService.findAll(text, title, pageable);
        return ResponseEntity.ok(news);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'JOURNALIST')")
    public ResponseEntity<NewsResponse> createNews(
            @Valid @RequestBody NewsRequest newsRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        newsRequest.setAuthor(userDetails.getUsername());
        NewsResponse newsResponse = newsService.create(newsRequest);
        return ResponseEntity
                .created(URI.create("/api/v1/news/" + newsResponse.getId()))
                .body(newsResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> getNewsById(@PathVariable Integer id) {
        NewsResponse news = newsService.find(id);
        return ResponseEntity.ok(news);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'JOURNALIST')")
    public ResponseEntity<NewsResponse> updateNews(
            @PathVariable Integer id,
            @Valid @RequestBody NewsRequest request
    ) {
        NewsResponse updatedNews = newsService.update(id, request);
        return ResponseEntity.ok(updatedNews);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'JOURNALIST')")
    public ResponseEntity<?> deleteNews(@PathVariable Integer id) {
        newsService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
