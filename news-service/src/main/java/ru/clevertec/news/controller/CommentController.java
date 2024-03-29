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
import ru.clevertec.logging.annotation.Loggable;
import ru.clevertec.news.controller.openapi.CommentOpenApi;
import ru.clevertec.news.dto.comment.CommentDTO;
import ru.clevertec.news.dto.comment.CommentRequest;
import ru.clevertec.news.dto.comment.CommentResponse;
import ru.clevertec.news.service.CommentService;

import java.net.URI;

@Loggable
@RestController
@RequestMapping("${api.path.prefix}")
@RequiredArgsConstructor
public class CommentController implements CommentOpenApi {

    private final CommentService commentService;

    @GetMapping("/comments")
    public ResponseEntity<Page<CommentDTO>> getComments(
            @RequestParam(required = false) String comment,
            Pageable pageable
    ) {
        Page<CommentDTO> comments = commentService.findAll(comment, pageable);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/news/{news_id}/comments")
    public ResponseEntity<Page<CommentDTO>> getCommentsByNewsId(
            @PathVariable("news_id") Integer newsId,
            Pageable pageable
    ) {
        Page<CommentDTO> comments = commentService.findByNewsId(newsId, pageable);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Integer id) {
        CommentResponse comment = commentService.find(id);
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/news/{news_id}/comments")
    @PreAuthorize("hasAnyRole('SUBSCRIBER', 'ADMIN', 'JOURNALIST')")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable("news_id") Integer newsId,
            @Valid @RequestBody CommentRequest commentRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        CommentResponse comment = commentService.create(newsId, userDetails.getUsername(), commentRequest);
        return ResponseEntity
                .created(URI.create("/comments/" + comment.getId()))
                .body(comment);
    }

    @PatchMapping("/comments/{id}")
    @PreAuthorize("hasAnyRole('SUBSCRIBER', 'ADMIN', 'JOURNALIST')")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Integer id,
            @Valid @RequestBody CommentRequest request
    ) {
        CommentResponse updatedComment = commentService.update(id, request);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/comments/{id}")
    @PreAuthorize("hasAnyRole('SUBSCRIBER', 'ADMIN', 'JOURNALIST')")
    public ResponseEntity<?> deleteComment(@PathVariable Integer id) {
        commentService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
