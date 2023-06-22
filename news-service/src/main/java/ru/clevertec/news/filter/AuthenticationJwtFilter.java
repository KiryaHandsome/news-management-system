package ru.clevertec.news.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.news.client.UserClient;
import ru.clevertec.news.dto.UserDetailsDto;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationJwtFilter extends OncePerRequestFilter {

    private final UserClient userClient;
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("Processing authentication...");
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        // Get user identity and set it on the spring security context
        log.info("Retrieving userDetails from user-service...");
        UserDetailsDto userDetailsDto = userClient.getUserDetails(token);
        UserDetails userDetails = new User(
                userDetailsDto.getUsername(), null,
                userDetailsDto.getAuthorities()
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );

        var authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Set authentication to SecurityContext");
        filterChain.doFilter(request, response);
    }
}
