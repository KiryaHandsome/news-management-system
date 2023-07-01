package ru.clevertec.news.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.news.cache.wrapper.LRUCache;
import ru.clevertec.news.dto.news.NewsResponse;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.util.TestConstants;
import ru.clevertec.news.util.TestData;

import javax.swing.text.html.Option;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private NewsRepository newsRepository;
    private ModelMapper mapper;
    private NewsService newsService;


    @BeforeEach
    void setUp() {
        mapper = new ModelMapper();
        newsService = new NewsService(mapper, cacheManager, newsRepository);
    }

    @Test
    void checkCreateShouldReturnExpectedResponse() {
        var news = TestData.getNews()
                .setId(null)
                .setCreatedAt(null)
                .setEditedAt(null);
        doReturn(TestData.getNews())
                .when(newsRepository)
                .save(news);

        var expected = mapper.map(TestData.getNews(), NewsResponse.class);
        var actual = newsService.create(TestConstants.AUTHOR, TestData.getNewsRequest());

        assertThat(actual).isEqualTo(expected);

        verify(newsRepository).save(news);
    }

    @Test
    void checkFindShouldReturnExpectedNews() {
        doReturn(Optional.of(TestData.getNews()))
                .when(newsRepository)
                .findById(TestConstants.NEWS_ID);

        var expected = TestData.getNewsResponse();
        var actual = newsService.find(TestConstants.NEWS_ID);

        assertThat(actual).isEqualTo(expected);

        verify(newsRepository).findById(TestConstants.NEWS_ID);
    }

    @Test
    void checkFindAllShouldReturnExpectedPage() {
        var pageable = PageRequest.of(0, 20);

        doReturn(new PageImpl<>(List.of(TestData.getNews())))
                .when(newsRepository)
                .findAll(null, null, pageable);

        var expected = new PageImpl<>(List.of(TestData.getNewsDTO()));
        var actual = newsService.findAll(null, null, pageable);

        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(1);
        assertThat(actual).isEqualTo(expected);

        verify(newsRepository).findAll(null, null, pageable);
    }

    @Test
    void checkUpdateShouldReturnExpectedResponse() {
        doReturn(Optional.of(TestData.getNews()))
                .when(newsRepository)
                .findById(TestConstants.NEWS_ID);

        doReturn(TestData.getNews())
                .when(newsRepository)
                .save(TestData.getNews());

        var expected = TestData.getNewsResponse();
        var actual = newsService.update(TestConstants.NEWS_ID, TestData.getNewsRequest());

        assertThat(actual).isEqualTo(expected);

        verify(newsRepository).save(TestData.getNews());
    }

    @Test
    void checkDeleteShouldCallDeleteById() {
        doReturn(new LRUCache(1, ""))
                .when(cacheManager)
                .getCache(NewsService.NEWS_CACHE_NAME);

        doReturn(new LRUCache(1, ""))
                .when(cacheManager)
                .getCache(CommentService.COMMENT_CACHE_NAME);

        newsService.delete(TestConstants.NEWS_ID);

        verify(newsRepository).deleteById(TestConstants.NEWS_ID);
    }
}