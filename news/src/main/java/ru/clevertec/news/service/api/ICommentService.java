package ru.clevertec.news.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.news.dto.comment.CommentDTO;
import ru.clevertec.news.dto.comment.CommentRequest;
import ru.clevertec.news.dto.comment.CommentResponse;

public interface ICommentService {

    CommentResponse find(Integer id);

    Page<CommentDTO> findByNewsId(Integer newsId, Pageable pageable);

    Page<CommentDTO> findAll(String comment, Pageable pageable);

    CommentResponse update(Integer id, CommentRequest entity);

    void delete(Integer id);

    CommentResponse create(Integer newsId, CommentRequest request);
}
