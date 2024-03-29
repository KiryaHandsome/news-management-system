package ru.clevertec.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.logging.annotation.Loggable;
import ru.clevertec.user.controller.openapi.UserOpenApi;
import ru.clevertec.user.dto.UserDetailsDto;

@Loggable
@RestController
@RequestMapping("${api.path.prefix}/users")
public class UserController implements UserOpenApi {

    @GetMapping("/info")
    public ResponseEntity<UserDetailsDto> getUserInfo(Authentication authentication) {
        UserDetailsDto response = new UserDetailsDto(
                authentication.getName(), authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList()
        );
        return ResponseEntity.ok(response);
    }
}
