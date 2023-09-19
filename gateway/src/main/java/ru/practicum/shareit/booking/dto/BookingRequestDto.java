package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.validation.annotation.StartBeforeEnd;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@StartBeforeEnd
public class BookingRequestDto {

    @FutureOrPresent
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    @PositiveOrZero
    private Integer itemId;
}
