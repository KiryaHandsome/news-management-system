package ru.clevertec.newssystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.newssystem.dto.comment.CommentDTO;
import ru.clevertec.newssystem.dto.comment.CommentRequest;
import ru.clevertec.newssystem.dto.comment.CommentResponse;
import ru.clevertec.newssystem.model.Comment;
import ru.clevertec.newssystem.service.CommentService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public ResponseEntity<Page<CommentDTO>> getComments(Pageable pageable) {
        Page<CommentDTO> comments = commentService.findAll(pageable);
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
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable("news_id") Integer newsId,
            @Valid @RequestBody CommentRequest commentRequest
    ) {
        CommentDTO comment = commentService.create(newsId, commentRequest);
        return ResponseEntity
                .created(URI.create("/comments/" + comment.getId()))
                .body(comment);
    }

    @PatchMapping("/comments/{id}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Integer id,
            @Valid @RequestBody CommentRequest request
    ) {
        CommentDTO updatedComment = commentService.update(id, request);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer id) {
        commentService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
