package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String message;

    private String error;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(String message, String error) {
        this.message = message;
        this.error = error;
    }
}
