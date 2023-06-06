package ru.clevertec.news.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.cache.annotation.CacheGet;
import ru.clevertec.news.cache.annotation.CacheRemove;
import ru.clevertec.news.cache.annotation.CacheSave;
import ru.clevertec.news.cache.annotation.EnableCache;
import ru.clevertec.news.dto.news.NewsDTO;
import ru.clevertec.news.dto.news.NewsRequest;
import ru.clevertec.news.dto.news.NewsResponse;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.model.News;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.service.api.INewsService;
import ru.clevertec.news.util.MapperUtil;


@Service
@EnableCache
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsService implements INewsService {

    private final ModelMapper mapper;
    private final NewsRepository newsRepository;

    static final String NEWS_CACHE_NAME = "news";

    @Override
    @CacheSave(NEWS_CACHE_NAME)
    @Transactional
    public NewsResponse create(NewsRequest request) {
        News news = mapper.map(request, News.class);
        News createdNews = newsRepository.save(news);
        return mapper.map(createdNews, NewsResponse.class);
    }

    @Override
    @CacheGet(NEWS_CACHE_NAME)
    public NewsResponse find(Integer id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "News with such id not found."));
        return mapper.map(news, NewsResponse.class);
    }

    @Override
    public Page<NewsDTO> findAll(String text, String title, Pageable pageable) {
        return newsRepository.findAll(text, title, pageable)
                .map(n -> mapper.map(n, NewsDTO.class));
    }

    @Override
    @CacheSave(NEWS_CACHE_NAME)
    @Transactional
    public NewsResponse update(Integer id, NewsRequest request) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "News with such id not found."));
        MapperUtil.mapNewsIfNotNull(news, request);
        News updatedNews = newsRepository.save(news);
        return mapper.map(updatedNews, NewsResponse.class);
    }

    @Override
    @CacheRemove(clearCache = true)
    @Transactional
    public void delete(Integer id) {
        newsRepository.deleteById(id);
    }
}
