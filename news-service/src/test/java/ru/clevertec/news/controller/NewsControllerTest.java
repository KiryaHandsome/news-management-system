package ru.clevertec.news.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.exception.EntityNotFoundException;
import ru.clevertec.news.dto.news.NewsDTO;
import ru.clevertec.news.dto.news.NewsRequest;
import ru.clevertec.news.integration.BaseIntegrationTest;
import ru.clevertec.news.service.NewsService;
import ru.clevertec.news.util.TestConstants;
import ru.clevertec.news.util.TestData;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class NewsControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @Autowired
    private ObjectMapper objectMapper;


    @Nested
    class GetNewsTest {

        @Test
        void shouldReturnExpectedNewsAndStatus200() throws Exception {
            var expectedNews = TestData.getNewsDTO();
            Page<NewsDTO> expected = new PageImpl<>(List.of(expectedNews));
            String url = TestConstants.GET_NEWS_URL +
                    String.format(
                            "?title=%s&text=%s",
                            TestConstants.NEWS_TITLE,
                            TestConstants.NEWS_TEXT
                    );

            doReturn(expected)
                    .when(newsService)
                    .findAll(TestConstants.NEWS_TEXT, TestConstants.NEWS_TITLE, TestConstants.PAGEABLE);

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(expected.getSize())))
                    .andExpect(jsonPath("$.content[0].id").value(expectedNews.getId()))
                    .andExpect(jsonPath("$.content[0].title").value(expectedNews.getTitle()));

            verify(newsService)
                    .findAll(TestConstants.NEWS_TEXT, TestConstants.NEWS_TITLE, TestConstants.PAGEABLE);
        }
    }

    @Nested
    class CreateNewsTest {

        @Test
        void shouldReturnExpectedNewsAndStatus201() throws Exception {
            var expectedNews = TestData.getNewsResponse();
            var request = TestData.getNewsRequest();
            String author = TestConstants.AUTHOR;
            String requestBody = objectMapper.writeValueAsString(request);

            doReturn(expectedNews)
                    .when(newsService)
                    .create(author, request);

            mockMvc.perform(post(TestConstants.CREATE_NEWS_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(expectedNews.getId()))
                    .andExpect(jsonPath("$.title").value(expectedNews.getTitle()))
                    .andExpect(jsonPath("$.text").value(expectedNews.getText()));

            verify(newsService)
                    .create(author, request);
        }

        @ParameterizedTest
        @MethodSource("ru.clevertec.news.controller.NewsControllerTest#provideBadNewsRequests")
        void shouldReturnStatus400(NewsRequest request) throws Exception {
            String requestBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post(TestConstants.CREATE_NEWS_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class GetNewsByIdTest {

        @Test
        void shouldReturnExpectedNewsAndStatus200() throws Exception {
            Integer id = TestConstants.NEWS_ID;
            var expectedNews = TestData.getNewsResponse();

            doReturn(expectedNews)
                    .when(newsService)
                    .find(id);

            mockMvc.perform(get(TestConstants.GET_NEWS_BY_ID_URL + id))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(expectedNews.getId()))
                    .andExpect(jsonPath("$.title").value(expectedNews.getTitle()))
                    .andExpect(jsonPath("$.text").value(expectedNews.getText()));

            verify(newsService)
                    .find(id);
        }

        @Test
        void shouldReturnStatus404() throws Exception {
            int id = Integer.MAX_VALUE;
            String url = TestConstants.GET_NEWS_BY_ID_URL + id;

            doThrow(new EntityNotFoundException(id, "Message"))
                    .when(newsService)
                    .find(id);

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound());

            verify(newsService)
                    .find(id);
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void shouldReturnExpectedNewsAndStatus200() throws Exception {
            Integer id = TestConstants.NEWS_ID;
            var expected = TestData.getNewsResponse();
            var request = TestData.getNewsRequest();
            String requestBody = objectMapper.writeValueAsString(request);

            doReturn(expected)
                    .when(newsService)
                    .update(id, request);

            mockMvc.perform(patch(TestConstants.UPDATE_NEWS_URL + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(expected.getId()))
                    .andExpect(jsonPath("$.title").value(expected.getTitle()))
                    .andExpect(jsonPath("$.text").value(expected.getText()));

            verify(newsService)
                    .update(id, request);
        }

        @Test
        void shouldReturnStatus404() throws Exception {
            int id = Integer.MAX_VALUE;
            var request = TestData.getNewsRequest();
            String requestBody = objectMapper.writeValueAsString(request);

            doThrow(new EntityNotFoundException(id, "Message"))
                    .when(newsService)
                    .update(id, request);

            mockMvc.perform(patch(TestConstants.UPDATE_NEWS_URL + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isNotFound());

            verify(newsService)
                    .update(id, request);
        }

        @ParameterizedTest
        @MethodSource("ru.clevertec.news.controller.NewsControllerTest#provideBadNewsRequests")
        void shouldReturnStatus400(NewsRequest request) throws Exception {
            int id = TestConstants.NEWS_ID;
            String requestBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(patch(TestConstants.UPDATE_NEWS_URL + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void shouldReturnStatus204() throws Exception {
            Integer id = TestConstants.COMMENT_ID;

            doNothing()
                    .when(newsService)
                    .delete(id);

            mockMvc.perform(delete(TestConstants.DELETE_NEWS_URL + id))
                    .andExpect(status().isNoContent());

            verify(newsService)
                    .delete(id);
        }
    }

    private static Stream<NewsRequest> provideBadNewsRequests() {
        return Stream.of(
                TestData.getNewsRequest().setText(null),
                TestData.getNewsRequest().setText(" "),
                TestData.getNewsRequest().setText(""),
                TestData.getNewsRequest().setTitle(null),
                TestData.getNewsRequest().setTitle(" "),
                TestData.getNewsRequest().setTitle("")
        );
    }
}