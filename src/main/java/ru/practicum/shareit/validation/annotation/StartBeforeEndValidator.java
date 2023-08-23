package ru.practicum.shareit.validation.annotation;

import ru.practicum.shareit.booking.dto.BookingShortDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, BookingShortDto> {

    @Override
    public void initialize(StartBeforeEnd constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookingShortDto bookingDto, ConstraintValidatorContext context) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            return false;
        }

        return bookingDto.getStart().isBefore(bookingDto.getEnd());
    }
}
