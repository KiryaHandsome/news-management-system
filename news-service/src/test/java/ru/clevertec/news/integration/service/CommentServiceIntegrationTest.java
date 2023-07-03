package ru.clevertec.news.integration.service;


import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.exception.EntityNotFoundException;
import ru.clevertec.news.dto.comment.CommentRequest;
import ru.clevertec.news.dto.comment.CommentResponse;
import ru.clevertec.news.integration.BaseIntegrationTest;
import ru.clevertec.news.model.Comment;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.util.TestConstants;
import ru.clevertec.news.util.TestData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class CommentServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CommentService commentService;

    @Nested
    class CreateTest {

        @Test
        void shouldReturnCreatedCommentWithId() {
            Integer newsId = 1;
            Comment comment = TestData.getComment().setId(null);
            CommentRequest request = mapper.map(comment, CommentRequest.class);

            CommentResponse response = commentService.create(newsId, "", request);

            assertThat(response).isNotNull();
            assertThat(response.getId()).isNotNull();
        }

        @Test
        void shouldThrowEntityNotFoundException() {
            Integer id = Integer.MAX_VALUE;
            assertThrows(EntityNotFoundException.class,
                    () -> commentService.create(id, TestConstants.AUTHOR, new CommentRequest()));
        }
    }

    @Nested
    class FindAllTest {

        @ParameterizedTest
        @ValueSource(strings = {"new", "old", "aaaaa"})
        void shouldReturnAllCommentsWhichContainsString(String s) {
            var comments = commentService.findAll(s, PageRequest.of(0, 20));
            assertThat(comments).isNotNull();
            for (var comment : comments) {
                assertThat(comment).isNotNull();
                assertThat(comment.getText()).contains(s);
            }
        }

        @Test
        void shouldReturnEmptyPage() {
            String s = "nonExistingStringForTest*&!@Y$Y!)@(Y)";

            var actual = commentService.findAll(s, PageRequest.of(0, 20));

            assertThat(actual).isNotNull();
            assertThat(actual.getNumberOfElements()).isEqualTo(0);
        }
    }

    @Nested
    class FindByNewsId {

        @Test
        void shouldReturnPageWithExpectedCommentsCount() {
            Integer newsId = 2;
            int expectedSize = 10;

            var comments = commentService.findByNewsId(newsId, PageRequest.of(0, 20));

            assertThat(comments).isNotNull();
            assertThat(comments).hasSize(expectedSize);
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void shouldReturnUpdatedComment() {
            Integer id = 5;
            String newText = "NEW TEXT";
            var comment = commentService.find(id);
            assertThat(comment).isNotNull();
            assertThat(comment.getText()).isNotEqualTo(newText);

            var actual = commentService.update(id, new CommentRequest(newText));
            assertThat(actual).isNotNull();
            assertThat(actual.getText()).isEqualTo(newText);
        }

        @Test
        void shouldThrowEntityNotFoundException() {
            Integer id = Integer.MAX_VALUE;
            assertThrows(EntityNotFoundException.class,
                    () -> commentService.update(id, null));
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void shouldThrowExceptionOnNextFind() {
            Integer id = 5;
            var comment = commentService.find(id);
            assertThat(comment).isNotNull();

            commentService.delete(id);

            assertThrows(EntityNotFoundException.class,
                    () -> commentService.find(id));
        }
    }
}
