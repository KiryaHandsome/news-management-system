package ru.clevertec.news.integration;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.dto.news.NewsDTO;
import ru.clevertec.news.dto.news.NewsRequest;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.model.News;
import ru.clevertec.news.service.NewsService;
import ru.clevertec.news.util.NewsBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@ActiveProfiles("dev")
public class NewsServiceIntegrationTest extends BaseIntegrationTest {


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

            Page<NewsDTO> all = newsService.findAll(null, null, PageRequest.of(0, 100));
            all.forEach(System.out::println);
            var actual = newsService.find(id);

            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(id);

        }

        @Test
        void shouldThrowEntityNotFoundException() {
            Integer id = Integer.MAX_VALUE;

            assertThrows(EntityNotFoundException.class,
                    () -> newsService.find(id));
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void shouldReturnNewsWithUpdatedTitle() {
            Integer id = 2;
            String newTitle = "NEWTITLE";
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
                    () -> newsService.find(id));
        }

        @Test
        @Transactional
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
