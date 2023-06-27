package ru.clevertec.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class IncorrectPasswordException extends RuntimeException {

    private final HttpStatus status = HttpStatus.UNAUTHORIZED;

    public IncorrectPasswordException(String message) {
        super(message);
    }
}
