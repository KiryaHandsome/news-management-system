package ru.clevertec.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.clevertec.user.model.User;
import ru.clevertec.user.service.api.IJwtService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService implements IJwtService {

    private final String secretKey;
    private final long jwtExpiration;
    private final String issuer;

    public JwtService(@Value("${application.security.jwt.secret-key}")
                      String secretKey,
                      @Value("${application.security.jwt.expiration}")
                      long jwtExpiration,
                      @Value("${application.security.jwt.issuer}")
                      String issuer) {
        this.secretKey = secretKey;
        this.jwtExpiration = jwtExpiration;
        this.issuer = issuer;
    }

    @Override
    public String extractUsername(String jwt) {
        Claims claims = extractClaims(jwt);
        return claims.getSubject();
    }

    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .setIssuer(issuer)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * Check if token is not expired
     *
     * @param jwt      token to check
     * @return true if token not expired, false - otherwise
     */
    @Override
    public boolean isExpired(String jwt) {
        Claims claims = extractClaims(jwt);
        Date expirationDate = claims.getExpiration();
        return expirationDate.before(new Date());
    }

    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
