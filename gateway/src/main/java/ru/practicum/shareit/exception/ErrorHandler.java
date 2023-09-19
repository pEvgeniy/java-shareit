package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.model.AccessToEntityDeniedException;
import ru.practicum.shareit.exception.model.EntityNotFoundException;
import ru.practicum.shareit.exception.model.ItemUnavailableException;
import ru.practicum.shareit.exception.model.WrongParamException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({
            AccessToEntityDeniedException.class,
            EntityNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundExceptions(Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage(), e.getClass().toString());
    }

    @ExceptionHandler({
            ItemUnavailableException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MissingRequestHeaderException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestExceptions(Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage(), e.getClass().toString());
    }

    @ExceptionHandler({
            DataIntegrityViolationException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictExceptions(Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage(), e.getClass().toString());
    }

    @ExceptionHandler({
            WrongParamException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenExceptions(Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage(), e.getClass().toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                e.getClass().toString(),
                e.getMessage()
        );
    }
}
