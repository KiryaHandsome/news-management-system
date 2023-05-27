package ru.clevertec.newssystem.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.newssystem.dto.ErrorEntity;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorEntity> handleRuntime(RuntimeException exception) {
        int statusCode = 500;
        String code = String.valueOf(statusCode);
        ErrorEntity errorEntity = new ErrorEntity(code, exception.getMessage());
        return ResponseEntity
                .status(statusCode)
                .body(errorEntity);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorEntity> handleEntityNotFound(EntityNotFoundException exception) {
        int statusCode = 404;
        String code = statusCode + String.valueOf(exception.getId());
        String message = exception.getMessage() + "(id=" + exception.getId() + ").";
        ErrorEntity errorEntity = new ErrorEntity(code, message);
        return ResponseEntity
                .status(statusCode)
                .body(errorEntity);
    }
}
