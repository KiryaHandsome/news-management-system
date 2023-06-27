package ru.clevertec.user.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import ru.clevertec.user.dto.LoginRequest;
import ru.clevertec.user.dto.LoginResponse;
import ru.clevertec.user.dto.UserRegisterRequest;
import ru.clevertec.user.exception.IncorrectPasswordException;


public interface AuthOpenApi {

    @Operation(
            summary = "User registration",
            description = "Register a new user.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User created"
                    )
            }
    )
    ResponseEntity<?> registerUser(@RequestBody @Valid UserRegisterRequest request);

    @Operation(
            summary = "Login user",
            description = "Login user in system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Username not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UsernameNotFoundException.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Password not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = IncorrectPasswordException.class)
                            )
                    )
            }
    )
    ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request);
}
