package ru.clevertec.newssystem.integration;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newssystem.dto.news.NewsRequest;
import ru.clevertec.newssystem.exception.EntityNotFoundException;
import ru.clevertec.newssystem.model.News;
import ru.clevertec.newssystem.service.NewsService;
import ru.clevertec.newssystem.util.NewsBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class NewsServiceIntegrationTest {


    @Autowired
    private NewsService newsService;

    @Autowired
    private ModelMapper mapper;
    private NewsBuilder NEWS_BUILDER;


    @BeforeEach
    void setUp() {
        NEWS_BUILDER = new NewsBuilder();
    }

    @Nested
    class FindAllTest {

        @Test
        void shouldReturnNotEmptyPage() {
            int expectedSize = 20;

            var actual = newsService.findAll(null, null, PageRequest.of(0, 20));

            assertThat(actual).isNotNull();
            assertThat(actual.getNumberOfElements()).isEqualTo(expectedSize);
        }

        @Test
        void shouldReturnElementWithExpectedTextAndTitle() {
            String title = "Breaking News";
            String text = "evaluations";

            var actual = newsService.findAll(text, title, PageRequest.of(0, 20));

            assertThat(actual).isNotNull();
            assertThat(actual.getNumberOfElements()).isPositive();
            for (var news : actual.getContent()) {
                assertThat(news.getTitle()).contains(title);
                assertThat(news.getText()).contains(text);
            }
        }

        @Test
        void shouldReturnEmptyPage() {
            String title = "UNEXISTINGTITLE";
            String text = "3q567890324-123489fj9@#$%^&*()_+";

            var actual = newsService.findAll(text, title, PageRequest.of(0, 20));

            assertThat(actual).isNotNull();
            assertThat(actual.getNumberOfElements()).isZero();
        }
    }

    @Nested
    class FindTest {

        @Test
        void shouldReturnNewsWithComments() {
            Integer id = 2;
            Pageable pageable = PageRequest.of(0, 20);

            var actual = newsService.find(id, pageable);

            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(id);
            assertThat(actual.getComments()).isNotNull();
            assertThat(actual.getComments()).isNotEmpty();

        }

        @Test
        void shouldThrowEntityNotFoundException() {
            Integer id = Integer.MAX_VALUE;

            assertThrows(EntityNotFoundException.class,
                    () -> newsService.find(id, PageRequest.of(0, 20)));
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void shouldReturnNewsWithUpdatedTitle() {
            String newTitle = "NEWTITLE";
            Integer id = 2;
            NewsRequest request = new NewsRequest(newTitle, null);

            var updatedNews = newsService.update(id, request);

            assertThat(updatedNews).isNotNull();
            assertThat(updatedNews.getId()).isEqualTo(id);
            assertThat(updatedNews.getTitle()).isEqualTo(newTitle);
        }

        @Test
        void shouldReturnNewsWithUpdatedText() {
            String newText = "NEWTEXT";
            Integer id = 2;
            NewsRequest request = new NewsRequest(null, newText);

            var updatedNews = newsService.update(id, request);

            assertThat(updatedNews).isNotNull();
            assertThat(updatedNews.getId()).isEqualTo(id);
            assertThat(updatedNews.getText()).isEqualTo(newText);
        }

        @Test
        void shouldThrowEntityNotFoundException() {
            Integer id = Integer.MAX_VALUE;

            assertThrows(EntityNotFoundException.class,
                    () -> newsService.update(id, new NewsRequest()));
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void shouldThrowExceptionOnFindCallAfterDeleting() {
            Integer id = 3;

            newsService.delete(id);

            assertThrows(EntityNotFoundException.class,
                    () -> newsService.find(id, PageRequest.of(0, 20)));
        }

        @Test
        void shouldDoesntThrowOnSecondDelete() {
            Integer id = 2;

            newsService.delete(id);

            assertDoesNotThrow(() -> newsService.delete(id));
        }
    }

    @Nested
    class CreateTest {

        @Test
        void shouldReturnCreatedNews() {
            News news = NEWS_BUILDER.build();
            NewsRequest request = mapper.map(news, NewsRequest.class);

            var actual = newsService.create(request);

            assertThat(actual).isNotNull();
            assertThat(actual.getText()).isEqualTo(request.getText());
            assertThat(actual.getTitle()).isEqualTo(request.getTitle());
            assertThat(actual.getCreatedAt()).isNotNull();
            assertThat(actual.getEditedAt()).isNotNull();
        }

        @Test
        void shouldThrowExceptionCauseOfNullFields() {
            NewsRequest request = new NewsRequest(null, null);

            assertThrows(RuntimeException.class,
                    () -> newsService.create(request));
        }
    }
}