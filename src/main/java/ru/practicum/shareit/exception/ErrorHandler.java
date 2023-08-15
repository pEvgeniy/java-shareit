package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.model.EmailNotUniqueException;
import ru.practicum.shareit.exception.model.EntityNotFoundException;
import ru.practicum.shareit.exception.model.WrongHeaderParamException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundExceptions(EntityNotFoundException e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage(), e.getClass().toString());
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MissingRequestHeaderException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestExceptions(Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage(), e.getClass().toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictExceptions(EmailNotUniqueException e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage(), e.getClass().toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenExceptions(WrongHeaderParamException e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage(), e.getClass().toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                "An unexpected error has occurred",
                e.getClass().toString()
        );
    }
}
