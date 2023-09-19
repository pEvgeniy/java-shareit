package ru.practicum.shareit.validation.annotation;

import ru.practicum.shareit.booking.dto.BookingRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, BookingRequestDto> {

    @Override
    public boolean isValid(BookingRequestDto bookingDto, ConstraintValidatorContext context) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            return false;
        }

        return bookingDto.getStart().isBefore(bookingDto.getEnd());
    }
}
