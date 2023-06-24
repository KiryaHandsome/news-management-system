package ru.clevertec.news.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.dto.comment.CommentDTO;
import ru.clevertec.news.dto.comment.CommentRequest;
import ru.clevertec.news.dto.comment.CommentResponse;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.model.Comment;
import ru.clevertec.news.model.News;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.service.api.ICommentService;
import ru.clevertec.news.util.MapperUtil;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final ModelMapper mapper;
    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;

    public static final String COMMENT_CACHE_NAME = "comments";

    @Override
    @CachePut(value = COMMENT_CACHE_NAME, key = "#result.id")
    @Transactional
    public CommentResponse create(Integer newsId, String author, CommentRequest request) {
        Comment comment = mapper.map(request, Comment.class);
        comment.setAuthor(author);
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new EntityNotFoundException(newsId, "News with such id not found"));
        comment.setNews(news);
        news.addComment(comment);
        commentRepository.saveAndFlush(comment);
        log.info("create({}, {})", newsId, request);
        return mapper.map(comment, CommentResponse.class);
    }

    @Override
    @Cacheable(COMMENT_CACHE_NAME)
    public CommentResponse find(Integer id) {
        log.info("find({})", id);
        return commentRepository.findById(id)
                .map(c -> mapper.map(c, CommentResponse.class))
                .orElseThrow(() -> new EntityNotFoundException(id, "Comment with such id not found."));
    }

    @Override
    public Page<CommentDTO> findByNewsId(Integer newsId, Pageable pageable) {
        log.info("findByNewsId({}, {})", newsId, pageable);
        return commentRepository.findByNewsId(newsId, pageable)
                .map(c -> mapper.map(c, CommentDTO.class));
    }

    @Override
    public Page<CommentDTO> findAll(String comment, Pageable pageable) {
        log.info("findAll({}, {})", comment, pageable);
        return commentRepository.findAll(comment, pageable)
                .map(c -> mapper.map(c, CommentDTO.class));
    }

    @Override
    @CachePut(value = COMMENT_CACHE_NAME, key = "#id")
    @Transactional
    public CommentResponse update(Integer id, CommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Comment with such id not found."));
        MapperUtil.mapCommentIfNotNull(comment, request);
        Comment updatedComment = commentRepository.saveAndFlush(comment);
        log.info("update({}, {})", id, request);
        return mapper.map(updatedComment, CommentResponse.class);
    }

    @Override
    @CacheEvict(COMMENT_CACHE_NAME)
    @Transactional
    public void delete(Integer id) {
        commentRepository.deleteById(id);
        log.info("delete({})", id);
    }
}
