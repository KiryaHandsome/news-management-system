package ru.clevertec.newssystem.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private final int id;

    public EntityNotFoundException(int id, String message) {
        super(message);
        this.id = id;
    }
}
