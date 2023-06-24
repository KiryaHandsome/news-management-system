package ru.clevertec.news.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.news.dto.ErrorEntity;
import ru.clevertec.news.dto.comment.CommentDTO;
import ru.clevertec.news.dto.comment.CommentRequest;
import ru.clevertec.news.dto.comment.CommentResponse;

@RestController
@RequestMapping("/comments")
@Validated
public interface CommentOpenApi {

    @Operation(
            summary = "Get comments",
            description = "Returns a list of comments."
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of comments",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    ResponseEntity<Page<CommentDTO>> getComments(
            @Parameter(description = "Filter comments by containing this comment text")
            @RequestParam(required = false) String comment,
            Pageable pageable
    );

    @Operation(
            summary = "Get comments by news ID",
            description = "Returns a list of comments for the specified news article."
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of comments by passed news id",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    ResponseEntity<Page<CommentDTO>> getCommentsByNewsId(
            @Parameter(description = "ID of the news article to retrieve comments for.")
            @PathVariable("news_id") Integer newsId,
            Pageable pageable
    );

    @Operation(
            summary = "Get comment by ID",
            description = "Returns the comment with the specified ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Comment not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorEntity.class)
                            )
                    )
            }
    )
    ResponseEntity<CommentResponse> getCommentById(@PathVariable Integer id);

    @Operation(
            summary = "Create comment",
            description = "Creates a new comment for the specified news article.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created comment",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "News not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorEntity.class),
                                    examples = @ExampleObject("""
                                            {
                                                 "status": 404,
                                                 "message": "News not found(id=5)"
                                            }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<CommentResponse> createComment(
            @Parameter(description = "ID of the news article to add the comment to.")
            @PathVariable("news_id") Integer newsId,
            @Valid @RequestBody CommentRequest commentRequest,
            @AuthenticationPrincipal UserDetails userDetails
    );

    @Operation(
            summary = "Update comment",
            description = "Updates the comment with the specified ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated comment",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Comment not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorEntity.class)
                            )
                    )
            }
    )
    ResponseEntity<CommentResponse> updateComment(
            @Parameter(description = "ID of the comment to update.") @PathVariable Integer id,
            @Valid @RequestBody CommentRequest request
    );

    @Operation(
            summary = "Delete comment",
            description = "Deletes the comment with the specified ID."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Comment deleted"
    )
    ResponseEntity<?> deleteComment(
            @Parameter(description = "ID of the comment to delete.")
            @PathVariable Integer id
    );
}
