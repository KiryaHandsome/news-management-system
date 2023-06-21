package ru.clevertec.news.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.clevertec.news.service.api.IJwtService;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;

@Component
public class JwtService implements IJwtService {

    private final String secretKey;

    public JwtService(@Value("${application.security.jwt.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public UserDetails extractUserDetails(String token) {
        Claims claims = extractClaims(token);
        String username = claims.getSubject();
        String role = (String) claims.get("role");
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        return new User(username, null, authorities);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
