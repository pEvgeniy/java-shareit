package ru.practicum.shareit.exception.model;

public class AccessToEntityDeniedException extends RuntimeException {
    public AccessToEntityDeniedException(String message) {
        super(message);
    }
}
