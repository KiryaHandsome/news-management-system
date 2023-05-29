package ru.clevertec.newssystem.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.newssystem.dto.comment.CommentDTO;
import ru.clevertec.newssystem.dto.comment.CommentRequest;
import ru.clevertec.newssystem.dto.comment.CommentResponse;

public interface ICommentService<ID extends Number> {

    CommentResponse find(ID id);

    Page<CommentDTO> findByNewsId(ID newsId, Pageable pageable);

    Page<CommentDTO> findAll(Pageable pageable);

    CommentDTO update(ID id, CommentRequest entity);

    void delete(ID id);

    CommentDTO create(ID newsId, CommentRequest request);
}
