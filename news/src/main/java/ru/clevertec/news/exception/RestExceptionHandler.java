package ru.clevertec.news.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.news.dto.ErrorEntity;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorEntity> handleRuntime(RuntimeException exception) {
        int status = 500;
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
        int status = 404;
        String code = status + String.valueOf(exception.getId());
        String message = exception.getMessage() + "(id=" + exception.getId() + ").";
        ErrorEntity error = new ErrorEntity(code, message);
        log.warn("Caught EntityNotFoundException with message: {}", message);
        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        int status = 400;
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
}
