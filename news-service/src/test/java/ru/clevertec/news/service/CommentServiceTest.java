package ru.clevertec.news.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.news.dto.comment.CommentDTO;
import ru.clevertec.news.dto.comment.CommentRequest;
import ru.clevertec.news.dto.comment.CommentResponse;
import ru.clevertec.news.model.Comment;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.util.MapperUtil;
import ru.clevertec.news.util.TestConstants;
import ru.clevertec.news.util.TestData;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NewsRepository newsRepository;

    private ModelMapper mapper;
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        mapper = new ModelMapper();
        commentService = new CommentService(new ModelMapper(), newsRepository, commentRepository);
    }

    @Test
    void checkCreateShouldReturnExpectedComment() {
        doReturn(Optional.of(TestData.getNews()))
                .when(newsRepository)
                .findById(TestConstants.NEWS_ID);

        var comment = TestData.getComment()
                .setId(null)
                .setCreatedAt(null)
                .setEditedAt(null);

        doReturn(TestData.getComment())
                .when(commentRepository)
                .saveAndFlush(any(Comment.class));

        var expected = mapper.map(comment, CommentResponse.class);

        var actual = commentService.create(
                TestConstants.NEWS_ID,
                TestConstants.AUTHOR,
                TestData.getCommentRequest()
        );

        verify(newsRepository).findById(TestConstants.NEWS_ID);
        verify(commentRepository).saveAndFlush(any(Comment.class));
    }

    @Test
    void checkFindShouldReturnExpectedCommentResponse() {
        doReturn(Optional.of(TestData.getComment()))
                .when(commentRepository)
                .findById(TestConstants.COMMENT_ID);

        var expected = mapper.map(TestData.getComment(), CommentResponse.class);
        var actual = commentService.find(TestConstants.COMMENT_ID);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);

        verify(commentRepository)
                .findById(TestConstants.COMMENT_ID);
    }

    @Test
    void findByNewsId() {
        var pageable = PageRequest.of(0, 20);
        doReturn(new PageImpl<>(List.of(TestData.getComment())))
                .when(commentRepository)
                .findByNewsId(TestConstants.NEWS_ID, pageable);

        commentService.findByNewsId(TestConstants.NEWS_ID, pageable);

        verify(commentRepository)
                .findByNewsId(TestConstants.NEWS_ID, pageable);
    }

    @Test
    void checkFindAllShouldReturnExpectedPage() {
        var pageable = PageRequest.of(0, 10);

        doReturn(new PageImpl<>(List.of(TestData.getComment())))
                .when(commentRepository)
                .findAll((String) null, pageable);

        var expected = new PageImpl<>(List.of(TestData.getCommentDTO()));
        var actual = commentService.findAll(null, pageable);

        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(1);
        assertThat(actual).isEqualTo(expected);

        verify(commentRepository)
                .findAll((String) null, pageable);
    }

    @Test
    void checkUpdateShouldReturnExpectedComment() {
        Comment comment = TestData.getComment();
        MapperUtil.mapCommentIfNotNull(comment, TestData.getCommentRequest());

        doReturn(Optional.of(TestData.getComment()))
                .when(commentRepository)
                .findById(TestConstants.COMMENT_ID);

        doReturn(TestData.getComment())
                .when(commentRepository)
                .saveAndFlush(comment);

        var expected = mapper.map(TestData.getComment(), CommentResponse.class);
        var actual = commentService.update(TestConstants.COMMENT_ID, TestData.getCommentRequest());

        assertThat(actual).isEqualTo(expected);

        verify(commentRepository)
                .findById(TestConstants.COMMENT_ID);
        verify(commentRepository)
                .saveAndFlush(comment);
    }

    @Test
    void checkDeleteShouldCallDeleteById() {
        commentService.delete(TestConstants.COMMENT_ID);

        verify(commentRepository)
                .deleteById(TestConstants.COMMENT_ID);
    }
}