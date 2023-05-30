package ru.clevertec.newssystem.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newssystem.cache.api.CacheProvider;
import ru.clevertec.newssystem.dto.comment.CommentDTO;
import ru.clevertec.newssystem.dto.comment.CommentRequest;
import ru.clevertec.newssystem.dto.comment.CommentResponse;
import ru.clevertec.newssystem.exception.EntityNotFoundException;
import ru.clevertec.newssystem.model.Comment;
import ru.clevertec.newssystem.model.News;
import ru.clevertec.newssystem.repository.CommentRepository;
import ru.clevertec.newssystem.repository.NewsRepository;
import ru.clevertec.newssystem.service.api.ICommentService;
import ru.clevertec.newssystem.util.MapperUtil;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService implements ICommentService<Integer> {

    private final ModelMapper mapper;
    private final NewsRepository newsRepository;
    private final CacheProvider cacheProvider;
    private final CommentRepository commentRepository;


    @Override
    @Transactional
    public CommentDTO create(Integer newsId, CommentRequest request) {
        Comment comment = mapper.map(request, Comment.class);
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new EntityNotFoundException(newsId, "News with such id not found"));
        comment.setNews(news);
        news.addComment(comment);
        commentRepository.saveAndFlush(comment);
        return mapper.map(comment, CommentDTO.class);
    }

    @Override
    public CommentResponse find(Integer id) {
        return commentRepository.findById(id)
                .map(c -> mapper.map(c, CommentResponse.class))
                .orElseThrow(() -> new EntityNotFoundException(id, "Comment with such id not found."));
    }

    @Override
    public Page<CommentDTO> findByNewsId(Integer newsId, Pageable pageable) {
        return commentRepository.findByNewsId(newsId, pageable)
                .map(c -> mapper.map(c, CommentDTO.class));
    }

    @Override
    public Page<CommentDTO> findAll(String comment, Pageable pageable) {
        return commentRepository.findAll(comment, pageable)
                .map(c -> mapper.map(c, CommentDTO.class));
    }

    @Override
    @Transactional
    public CommentDTO update(Integer id, CommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Comment with such id not found."));
        MapperUtil.mapCommentIfNotNull(comment, request);
        Comment updatedComment = commentRepository.save(comment);
        return mapper.map(updatedComment, CommentDTO.class);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        commentRepository.deleteById(id);
    }
}
