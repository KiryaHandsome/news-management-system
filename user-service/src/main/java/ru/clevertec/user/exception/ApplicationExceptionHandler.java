package ru.clevertec.user.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.user.dto.ErrorEntity;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorEntity> handleIncorrectPassword(IncorrectPasswordException ex) {
        int status = HttpStatus.UNAUTHORIZED.value();
        ErrorEntity error = new ErrorEntity(status, ex.getMessage());
        log.warn("Caught IncorrectPasswordException with message: {}", ex.getMessage());
        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorEntity> handleIncorrectPassword(UsernameNotFoundException ex) {
        int status = HttpStatus.UNAUTHORIZED.value();
        ErrorEntity error = new ErrorEntity(status, ex.getMessage());
        log.warn("Caught UsernameNotFoundException with message: {}", ex.getMessage());
        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(BaseRestException.class)
    public ResponseEntity<ErrorEntity> handleBaseRest(BaseRestException ex) {
        int status = ex.getStatus().value();
        ErrorEntity error = new ErrorEntity(status, ex.getMessage());
        log.warn("Caught BaseRestException with message: {}; status: {}", ex.getMessage(), status);
        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        int status = HttpStatus.BAD_REQUEST.value();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        log.warn("Caught MethodArgumentNotValidException with message: {}", ex.getMessage());
        return ResponseEntity
                .status(status)
                .body(errors);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorEntity> handleRuntime(RuntimeException ex) {
        int status = HttpStatus.FORBIDDEN.value();
        ErrorEntity error = new ErrorEntity(status, ex.getMessage());
        log.warn("Caught RuntimeException with message: {}", ex.getMessage());
        return ResponseEntity
                .status(status)
                .body(error);
    }
}
