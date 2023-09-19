package ru.practicum.shareit.exception.model;

public class WrongParamException extends RuntimeException {
    public WrongParamException(String message) {
        super(message);
    }
}
