package ru.clevertec.user.exception;

import org.springframework.http.HttpStatus;

public class IncorrectPasswordException extends BaseRestException {

    public IncorrectPasswordException(HttpStatus status, String message) {
        super(status, message);
    }
}
