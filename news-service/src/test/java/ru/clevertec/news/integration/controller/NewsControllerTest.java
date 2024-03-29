package ru.clevertec.news.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.exception.EntityNotFoundException;
import ru.clevertec.news.dto.UserDetailsDto;
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
@AutoConfigureMockMvc
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
        @WithMockUser(roles = "JOURNALIST", username = TestConstants.AUTHOR)
        void shouldReturnExpectedNewsAndStatus201() throws Exception {
            var expected = TestData.getNewsResponse();
            var request = TestData.getNewsRequest();
            String requestBody = objectMapper.writeValueAsString(request);

            doReturn(expected)
                    .when(newsService)
                    .create(TestConstants.AUTHOR, request);

            mockMvc.perform(post(TestConstants.CREATE_NEWS_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(expected.getId()))
                    .andExpect(jsonPath("$.title").value(expected.getTitle()))
                    .andExpect(jsonPath("$.text").value(expected.getText()));

            verify(newsService)
                    .create(TestConstants.AUTHOR, request);
        }

        @WithMockUser(roles = "JOURNALIST")
        @ParameterizedTest
        @MethodSource("ru.clevertec.news.integration.controller.NewsControllerTest#provideBadNewsRequests")
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
        @WithMockUser(roles = "JOURNALIST")
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
        @WithMockUser(roles = "JOURNALIST")
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

        @Test
        void shouldReturnStatus403() throws Exception {
            int id = Integer.MAX_VALUE;
            var request = TestData.getNewsRequest();
            String requestBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(patch(TestConstants.UPDATE_NEWS_URL + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isForbidden());
        }

        @WithMockUser(roles = "JOURNALIST")
        @ParameterizedTest
        @MethodSource("ru.clevertec.news.integration.controller.NewsControllerTest#provideBadNewsRequests")
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
        @WithMockUser(roles = "JOURNALIST")
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

        @Test
        void shouldReturnStatus403() throws Exception {
            Integer id = TestConstants.COMMENT_ID;

            mockMvc.perform(delete(TestConstants.DELETE_NEWS_URL + id))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @WireMockTest(httpPort = 8081)
    class NewsWireMockTest {

        @Test
        void checkDeleteForSubscriberShouldReturnStatus403() throws Exception {
            var response = new UserDetailsDto("Kirya", List.of("ROLE_SUBSCRIBER"));
            WireMock.stubFor(
                    WireMock.get(WireMock.urlEqualTo("/api/v1/users/info"))
                            .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo(TestConstants.TOKEN))
                            .willReturn(WireMock.aResponse()
                                    .withStatus(200)
                                    .withHeader("Content-Type", "application/json")
                                    .withBody(objectMapper.writeValueAsString(response))
                            )
            );

            Integer id = TestConstants.COMMENT_ID;

            mockMvc.perform(delete(TestConstants.DELETE_NEWS_URL + id)
                            .header(HttpHeaders.AUTHORIZATION, TestConstants.TOKEN))
                    .andExpect(status().isForbidden());
        }

        @Test
        void checkDeleteForSubscriberShouldReturnStatus204() throws Exception {
            var response = new UserDetailsDto("Kirya", List.of("ROLE_JOURNALIST"));
            WireMock.stubFor(
                    WireMock.get(WireMock.urlEqualTo("/api/v1/users/info"))
                            .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo(TestConstants.TOKEN))
                            .willReturn(WireMock.aResponse()
                                    .withStatus(200)
                                    .withHeader("Content-Type", "application/json")
                                    .withBody(objectMapper.writeValueAsString(response))
                            )
            );

            Integer id = TestConstants.COMMENT_ID;

            mockMvc.perform(delete(TestConstants.DELETE_NEWS_URL + id)
                            .header(HttpHeaders.AUTHORIZATION, TestConstants.TOKEN))
                    .andExpect(status().isNoContent());
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