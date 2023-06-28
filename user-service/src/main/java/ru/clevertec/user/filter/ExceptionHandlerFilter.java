package ru.clevertec.user.filter;

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
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.user.dto.ErrorEntity;

import java.io.IOException;

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
        try {
            filterChain.doFilter(request, response);
        } catch (ClaimJwtException ex) {
            writeErrorResponse(response, HttpStatus.UNAUTHORIZED, ex.getMessage());
        } catch (RuntimeException | IOException ex) {
            writeErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    private void writeErrorResponse(
            HttpServletResponse response,
            HttpStatus status,
            String message
    ) throws IOException {
        response.setStatus(status.value());
        ErrorEntity error = new ErrorEntity(status.value(), message);
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}