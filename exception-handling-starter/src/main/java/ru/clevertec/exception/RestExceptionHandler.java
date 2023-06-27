package ru.clevertec.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorEntity> handleRuntime(RuntimeException exception) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String code = String.valueOf(status);
        String message = exception.getMessage();
        ErrorEntity error = new ErrorEntity(code, message);
        log.error("Caught RuntimeException with message: {}", message);
        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorEntity> handleEntityNotFound(EntityNotFoundException exception) {
        int status = HttpStatus.NOT_FOUND.value();
        String code = status + String.valueOf(exception.getId());
        String message = exception.getMessage() + "(id=" + exception.getId() + ").";
        ErrorEntity error = new ErrorEntity(code, message);
        log.warn("Caught EntityNotFoundException with message: {}", message);
        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleArgumentNotValid(MethodArgumentNotValidException ex) {
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

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorEntity> handleIncorrectPassword(IncorrectPasswordException ex) {
        int status = ex.getStatus().value();
        ErrorEntity error = new ErrorEntity(String.valueOf(status), ex.getMessage());
        log.warn("Caught IncorrectPasswordException with message: {}", ex.getMessage());
        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorEntity> handleUsernameNotFound(UsernameNotFoundException ex) {
        int status = HttpStatus.UNAUTHORIZED.value();
        ErrorEntity error = new ErrorEntity(String.valueOf(status), ex.getMessage());
        log.warn("Caught UsernameNotFoundException with message: {}", ex.getMessage());
        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorEntity> handleAccessDenied(AccessDeniedException ex) {
        int status = HttpStatus.FORBIDDEN.value();
        ErrorEntity error = new ErrorEntity(String.valueOf(status), "You don't have permission for this action");
        log.warn("Caught AccessDeniedException with message: {}", ex.getMessage());
        return ResponseEntity
                .status(status)
                .body(error);
    }
}
