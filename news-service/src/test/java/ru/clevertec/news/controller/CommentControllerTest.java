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
import ru.clevertec.news.dto.comment.CommentDTO;
import ru.clevertec.news.dto.comment.CommentRequest;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.util.TestConstants;
import ru.clevertec.news.util.TestData;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
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
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class GetCommentsTest {

        @Test
        void shouldReturnExpectedCommentAndStatus200() throws Exception {
            var expectedComment = TestData.getCommentDTO();
            Page<CommentDTO> expected = new PageImpl<>(List.of(expectedComment));
            String url = TestConstants.GET_COMMENTS_URL + "?comment=" + TestConstants.COMMENT_TEXT;

            doReturn(expected)
                    .when(commentService)
                    .findAll(TestConstants.COMMENT_TEXT, TestConstants.PAGEABLE);

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(expected.getSize())))
                    .andExpect(jsonPath("$.content[0].id").value(expectedComment.getId()))
                    .andExpect(jsonPath("$.content[0].text").value(expectedComment.getText()));

            verify(commentService)
                    .findAll(TestConstants.COMMENT_TEXT, TestConstants.PAGEABLE);
        }
    }

    @Nested
    class GetCommentsByNewsIdTest {

        @Test
        void shouldReturnExpectedCommentAndStatus200() throws Exception {
            var comment = TestData.getCommentDTO();
            Page<CommentDTO> expected = new PageImpl<>(List.of(comment));
            Integer newsId = TestConstants.NEWS_ID;
            String url = String.format(TestConstants.GET_COMMENTS_BY_NEWS_ID_TEMPLATE_URL, newsId);

            doReturn(expected)
                    .when(commentService)
                    .findByNewsId(newsId, TestConstants.PAGEABLE);

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(expected.getSize())))
                    .andExpect(jsonPath("$.content[0].id").value(comment.getId()))
                    .andExpect(jsonPath("$.content[0].text").value(comment.getText()));

            verify(commentService)
                    .findByNewsId(newsId, TestConstants.PAGEABLE);
        }

        @Test
        void shouldReturnStatus404() throws Exception {
            int newsId = Integer.MAX_VALUE;
            String url = String.format(TestConstants.GET_COMMENTS_BY_NEWS_ID_TEMPLATE_URL, newsId);

            doThrow(new EntityNotFoundException(newsId, "Message"))
                    .when(commentService)
                    .findByNewsId(newsId, TestConstants.PAGEABLE);

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound());

            verify(commentService)
                    .findByNewsId(newsId, TestConstants.PAGEABLE);
        }
    }

    @Nested
    class GetCommentByIdTest {

        @Test
        void shouldReturnExpectedCommentAndStatus200() throws Exception {
            var comment = TestData.getCommentResponse();

            doReturn(comment)
                    .when(commentService)
                    .find(comment.getId());

            mockMvc.perform(get(TestConstants.GET_COMMENT_BY_ID_URL + comment.getId()))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(comment.getId()))
                    .andExpect(jsonPath("$.text").value(comment.getText()))
                    .andExpect(jsonPath("$.news.id").value(comment.getNews().getId()))
                    .andExpect(jsonPath("$.news.title").value(comment.getNews().getTitle()));

            verify(commentService)
                    .find(comment.getId());
        }

        @Test
        void shouldReturnStatus404() throws Exception {
            Integer commentId = TestConstants.COMMENT_ID;

            doThrow(new EntityNotFoundException(commentId, "Message"))
                    .when(commentService)
                    .find(commentId);

            mockMvc.perform(get(TestConstants.GET_COMMENT_BY_ID_URL + commentId))
                    .andExpect(status().isNotFound());

            verify(commentService)
                    .find(commentId);
        }
    }


    @Nested
    class CreateCommentTest {

        @Test
        void shouldReturnCreatedCommentAndStatus201() throws Exception {
            Integer newsId = TestConstants.NEWS_ID;
            String author = "author";
            var request = TestData.getCommentRequest();
            var expected = TestData.getCommentResponse();
            String requestBody = objectMapper.writeValueAsString(request);
            String url = String.format(TestConstants.CREATE_COMMENT_TEMPLATE_URL, newsId);

            doReturn(expected)
                    .when(commentService)
                    .create(newsId, author, request);

            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(expected.getId()))
                    .andExpect(jsonPath("$.text").value(expected.getText()))
                    .andExpect(jsonPath("$.news.title").value(expected.getNews().getTitle()))
                    .andExpect(jsonPath("$.news.id").value(expected.getNews().getId()));

            verify(commentService)
                    .create(newsId, author, request);
        }

        @Test
        void shouldReturnStatus404() throws Exception {
            Integer newsId = TestConstants.NEWS_ID;
            var request = TestData.getCommentRequest();
            String author = "author";
            String url = String.format(TestConstants.CREATE_COMMENT_TEMPLATE_URL, newsId);
            String requestBody = objectMapper.writeValueAsString(request);

            doThrow(new EntityNotFoundException(newsId, "Message"))
                    .when(commentService)
                    .create(newsId, author, request);

            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isNotFound());

            verify(commentService)
                    .create(newsId, author, request);
        }

        @ParameterizedTest
        @MethodSource("ru.clevertec.news.controller.CommentControllerTest#provideBadCommentRequests")
        void shouldReturnStatus400(CommentRequest request) throws Exception {
            int newsId = TestConstants.NEWS_ID;
            String requestBody = objectMapper.writeValueAsString(request);
            String url = String.format(TestConstants.CREATE_COMMENT_TEMPLATE_URL, newsId);

            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateCommentTest {

        @Test
        void shouldReturnUpdatedCommentAndStatus200() throws Exception {
            Integer id = TestConstants.COMMENT_ID;
            var request = TestData.getCommentRequest();
            var expected = TestData.getCommentResponse();
            String requestBody = objectMapper.writeValueAsString(expected);

            doReturn(expected)
                    .when(commentService)
                    .update(id, request);

            mockMvc.perform(patch(TestConstants.UPDATE_COMMENT_URL + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(expected.getId()))
                    .andExpect(jsonPath("$.text").value(expected.getText()))
                    .andExpect(jsonPath("$.news.title").value(expected.getNews().getTitle()))
                    .andExpect(jsonPath("$.news.id").value(expected.getNews().getId()));

            verify(commentService)
                    .update(id, request);
        }

        @Test
        void shouldReturnStatus404() throws Exception {
            Integer id = TestConstants.COMMENT_ID;
            var request = TestData.getCommentRequest();
            String url = TestConstants.UPDATE_COMMENT_URL + id;
            String requestBody = objectMapper.writeValueAsString(request);

            doThrow(new EntityNotFoundException(id, "Message"))
                    .when(commentService)
                    .update(id, request);

            mockMvc.perform(patch(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isNotFound());

            verify(commentService)
                    .update(id, request);
        }

        @ParameterizedTest
        @MethodSource("ru.clevertec.news.controller.CommentControllerTest#provideBadCommentRequests")
        void shouldReturnStatus400(CommentRequest request) throws Exception {
            int id = TestConstants.NEWS_ID;
            String requestBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(patch(TestConstants.UPDATE_COMMENT_URL + id)
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

            mockMvc.perform(delete(TestConstants.DELETE_COMMENT_URL + id))
                    .andExpect(status().isNoContent());

            verify(commentService)
                    .delete(id);
        }
    }

    private static Stream<CommentRequest> provideBadCommentRequests() {
        return Stream.of(
                TestData.getCommentRequest().setText(null),
                TestData.getCommentRequest().setText(" "),
                TestData.getCommentRequest().setText("")
        );
    }

}