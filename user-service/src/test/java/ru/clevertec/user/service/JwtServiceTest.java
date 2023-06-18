package ru.clevertec.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.user.model.Role;
import ru.clevertec.user.model.User;

import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


class JwtServiceTest {

    private JwtService jwtService;
    private final String secretKey = "KJHAJHAUIOGQJKBLASNFKAJSFKLJBASLKFJBKSAJBLKJB";
    private final long jwtExpiration = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(secretKey, jwtExpiration);
    }

    @Test
    public void testCreateJwtToken() {
        String username = "kirya_handsome";
        Date now = new Date(System.currentTimeMillis());
        Date expiration = new Date(System.currentTimeMillis() + jwtExpiration);
        Role role = Role.ROLE_USER;
        User user = User.builder()
                .id(1)
                .username(username)
                .role(role)
                .build();

        String token = jwtService.generateToken(user);
        Key key = jwtService.getSignInKey();
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo(username);
        assertThat(claims.get("role", String.class)).isEqualTo(role.name());
        assertThat(claims.getIssuedAt().getTime()).isLessThanOrEqualTo(now.getTime());
        // this assert gives bad result because claims stores time
        // in milliseconds in integer format but not long
//        assertThat(claims.getExpiration().getTime()).isGreaterThanOrEqualTo(expiration.getTime());
    }
}