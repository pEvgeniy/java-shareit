package ru.practicum.shareit.exception.model;

public class WrongHeaderParamException extends RuntimeException {
    public WrongHeaderParamException(String message) {
        super(message);
    }
}
