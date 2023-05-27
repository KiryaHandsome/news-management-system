package ru.clevertec.newssystem.exception;

import lombok.Getter;

@Getter
public class RestException extends RuntimeException {

    private final int code;

    public RestException(int code, String message) {
        super(message);
        this.code = code;
    }
}
