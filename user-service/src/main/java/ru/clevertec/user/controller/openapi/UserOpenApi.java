package ru.clevertec.user.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.clevertec.user.dto.UserDetailsDto;


public interface UserOpenApi {

    @Operation(
            summary = "User info",
            description = "Get current user info."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Current user info",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDetailsDto.class)
            )
    )
    ResponseEntity<UserDetailsDto> getUserInfo(Authentication authentication);
}
