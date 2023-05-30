package ru.clevertec.newssystem.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newssystem.cache.api.CacheProvider;
import ru.clevertec.newssystem.dto.comment.CommentDTO;
import ru.clevertec.newssystem.dto.news.NewsDTO;
import ru.clevertec.newssystem.dto.news.NewsRequest;
import ru.clevertec.newssystem.dto.news.NewsResponse;
import ru.clevertec.newssystem.exception.EntityNotFoundException;
import ru.clevertec.newssystem.model.News;
import ru.clevertec.newssystem.repository.CommentRepository;
import ru.clevertec.newssystem.repository.NewsRepository;
import ru.clevertec.newssystem.service.api.INewsService;
import ru.clevertec.newssystem.util.MapperUtil;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsService implements INewsService<Integer> {

    private final ModelMapper mapper;
    private final CacheProvider cacheProvider;
    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public NewsDTO create(NewsRequest request) {
        News news = mapper.map(request, News.class);
        News createdNews = newsRepository.save(news);
        return mapper.map(createdNews, NewsDTO.class);
    }

    @Override
    public NewsResponse find(Integer id, Pageable pageable) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "News with such id not found."));
        NewsResponse response = mapper.map(news, NewsResponse.class);
        Page<CommentDTO> comments = commentRepository.findByNewsId(id, pageable)
                .map(c -> mapper.map(c, CommentDTO.class));
        response.setComments(comments);
        return response;
    }

    @Override
    public Page<NewsDTO> findAll(String text, String title, Pageable pageable) {
        return newsRepository.findAll(text, title, pageable)
                .map(n -> mapper.map(n, NewsDTO.class));
    }

    @Override
    @Transactional
    public NewsDTO update(Integer id, NewsRequest request) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "News with such id not found."));
        MapperUtil.mapNewsIfNotNull(news, request);
        News updatedNews = newsRepository.save(news);
        return mapper.map(updatedNews, NewsDTO.class);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        newsRepository.deleteById(id);
    }
}
