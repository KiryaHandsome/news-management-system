package ru.clevertec.newssystem.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newssystem.dto.CommentRequest;
import ru.clevertec.newssystem.dto.CommentResponse;
import ru.clevertec.newssystem.exception.EntityNotFoundException;
import ru.clevertec.newssystem.model.Comment;
import ru.clevertec.newssystem.model.News;
import ru.clevertec.newssystem.repository.CommentRepository;
import ru.clevertec.newssystem.service.api.ICommentService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService implements ICommentService<Integer> {

    private final ModelMapper mapper;
    private final EntityManager entityManager;
    private final CommentRepository commentRepository;


    @Override
    @Transactional
    public CommentResponse create(Integer newsId, CommentRequest request) {
        Comment comment = mapper.map(request, Comment.class);
        News news = entityManager.getReference(News.class, newsId);
        comment.setNews(news);
        Comment createdComment = commentRepository.save(comment);
        return mapper.map(createdComment, CommentResponse.class);
    }

    @Override
    public CommentResponse find(Integer id) {
        return commentRepository.findById(id)
                .map(c -> mapper.map(c, CommentResponse.class))
                .orElseThrow(() -> new EntityNotFoundException(id, "Comment with such id not found."));
    }

    @Override
    public Page<CommentResponse> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable)
                .map(c -> mapper.map(c, CommentResponse.class));
    }

    @Override
    @Transactional
    public CommentResponse update(Integer id, CommentRequest request) {
        Comment comment = mapper.map(request, Comment.class);
        comment.setId(id);
        Comment updatedComment = commentRepository.save(comment);
        return mapper.map(updatedComment, CommentResponse.class);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        commentRepository.deleteById(id);
    }
}
