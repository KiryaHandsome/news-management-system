package ru.clevertec.news.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.exception.EntityNotFoundException;
import ru.clevertec.news.dto.news.NewsDTO;
import ru.clevertec.news.dto.news.NewsRequest;
import ru.clevertec.news.dto.news.NewsResponse;
import ru.clevertec.news.model.News;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.service.api.INewsService;
import ru.clevertec.news.util.MapperUtil;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsService implements INewsService {

    private final ModelMapper mapper;
    private final CacheManager cacheManager;
    private final NewsRepository newsRepository;

    public static final String NEWS_CACHE_NAME = "news";

    @Override
    @CachePut(value = NEWS_CACHE_NAME, key = "#result.id")
    @Transactional
    public NewsResponse create(String author, NewsRequest request) {
        News news = mapper.map(request, News.class);
        news.setAuthor(author);
        News createdNews = newsRepository.save(news);
        log.info("create({})", request);
        return mapper.map(createdNews, NewsResponse.class);
    }

    @Override
    @Cacheable(NEWS_CACHE_NAME)
    public NewsResponse find(Integer id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "News with such id not found."));
        log.info("find({})", id);
        return mapper.map(news, NewsResponse.class);
    }

    @Override
    public Page<NewsDTO> findAll(String text, String title, Pageable pageable) {
        log.info("findAll({}, {}, {})", text, title, pageable);
        return newsRepository.findAll(text, title, pageable)
                .map(n -> mapper.map(n, NewsDTO.class));
    }

    @Override
    @CachePut(value = NEWS_CACHE_NAME, key = "#id")
    @Transactional
    public NewsResponse update(Integer id, NewsRequest request) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "News with such id not found."));
        MapperUtil.mapNewsIfNotNull(news, request);
        News updatedNews = newsRepository.save(news);
        log.info("update({}, {})", id, request);
        return mapper.map(updatedNews, NewsResponse.class);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        clearCaches(id);
        newsRepository.deleteById(id);
        log.info("delete({})", id);
    }

    /**
     * Method that removes news by id from its cache
     * and clears comment cache entirely because of possible connection
     * between news and comments.
     *
     * @param newsId id of news to evict
     */
    private void clearCaches(Integer newsId) {
        cacheManager.getCache(NEWS_CACHE_NAME).evict(newsId);
        cacheManager.getCache(CommentService.COMMENT_CACHE_NAME).clear();
    }
}
