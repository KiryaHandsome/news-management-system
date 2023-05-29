package ru.clevertec.newssystem.integration;


import org.junit.jupiter.api.BeforeEach;
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
import ru.clevertec.newssystem.dto.comment.CommentDTO;
import ru.clevertec.newssystem.dto.comment.CommentRequest;
import ru.clevertec.newssystem.exception.EntityNotFoundException;
import ru.clevertec.newssystem.model.Comment;
import ru.clevertec.newssystem.service.CommentService;
import ru.clevertec.newssystem.util.CommentBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class CommentServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ModelMapper mapper;
    private CommentBuilder COMMENT_BUILDER;


    @BeforeEach
    void setUp() {
        COMMENT_BUILDER = new CommentBuilder();
    }

    @Nested
    class CreateTest {

        @Test
        void shouldReturnCreatedCommentWithId() {
            Integer newsId = 1;
            Comment comment = COMMENT_BUILDER.withId(null).build();
            CommentRequest request = mapper.map(comment, CommentRequest.class);

            CommentDTO response = commentService.create(newsId, request);

            assertThat(response).isNotNull();
            assertThat(response.getId()).isNotNull();
            System.out.println(response);
        }

        @Test
        void shouldThrowEntityNotFoundException() {
            Integer id = Integer.MAX_VALUE;
            assertThrows(EntityNotFoundException.class,
                    () -> commentService.create(id, new CommentRequest()));
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
