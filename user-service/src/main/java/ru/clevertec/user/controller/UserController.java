package ru.clevertec.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.user.model.User;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public ResponseEntity<Collection<?>> getUserInfo(Authentication authentication) {
        var authorities = authentication.getAuthorities();
        return ResponseEntity.ok(authorities);
    }
}
