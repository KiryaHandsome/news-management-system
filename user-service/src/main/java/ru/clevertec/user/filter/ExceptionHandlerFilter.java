package ru.clevertec.user.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ClaimJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.user.dto.ErrorEntity;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    /**
     * Filter for handling exceptions during filtering.
     * It must goes before custom filters.
     */
    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain) throws ServletException, IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Internal server error";
        try {
            filterChain.doFilter(request, response);
        } catch (ClaimJwtException ex) {
            status = HttpStatus.UNAUTHORIZED;
            message = ex.getMessage();
        } catch (RuntimeException | IOException ex) {
            message = ex.getMessage();
        } finally {
            response.setStatus(status.value());
            ErrorEntity error = new ErrorEntity(status.value(), message);
            response.getWriter().write(objectMapper.writeValueAsString(error));
        }
    }
}