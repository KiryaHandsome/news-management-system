package ru.clevertec.news.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.exception.ErrorEntity;
import ru.clevertec.news.dto.news.NewsDTO;
import ru.clevertec.news.dto.news.NewsRequest;
import ru.clevertec.news.dto.news.NewsResponse;


public interface NewsOpenApi
{
    @Operation(
            summary = "Get news",
            description = "Returns a list of news."
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of news",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)
            )
    )
    ResponseEntity<Page<NewsDTO>> getNews(
            @Parameter(description = "Filter news by containing this news text")
            @RequestParam(required = false) String text,
            @Parameter(description = "Filter news by containing this title text")
            @RequestParam(required = false) String title,
            Pageable pageable);

    @Operation(
            summary = "Create news",
            description = "Creates a new news."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Created news",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = NewsResponse.class)
            )
    )
    ResponseEntity<NewsResponse> createNews(
            @Valid @RequestBody NewsRequest newsRequest,
            @AuthenticationPrincipal UserDetails userDetails
    );

    @Operation(
            summary = "Get news by ID",
            description = "Returns the news with the specified ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NewsResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "News not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorEntity.class)
                            )
                    )
            }
    )
    ResponseEntity<NewsResponse> getNewsById(@PathVariable Integer id);

    @Operation(
            summary = "Update news",
            description = "Updates the news with the specified ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated news",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NewsResponse.class)
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
    ResponseEntity<NewsResponse> updateNews(
            @Parameter(description = "ID of the news to update.")
            @PathVariable Integer id,
            @Valid @RequestBody NewsRequest request
    );

    @Operation(
            summary = "Delete news",
            description = "Deletes the news with the specified ID."
    )
    @ApiResponse(
            responseCode = "204",
            description = "News deleted"
    )
    ResponseEntity<?> deleteNews(
            @Parameter(description = "ID of the news to delete.")
            @PathVariable Integer id
    );
}
