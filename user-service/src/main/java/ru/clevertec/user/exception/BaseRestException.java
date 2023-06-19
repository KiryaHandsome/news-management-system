package ru.clevertec.user.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseRestException extends RuntimeException {
    private final HttpStatus status;

    public BaseRestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
