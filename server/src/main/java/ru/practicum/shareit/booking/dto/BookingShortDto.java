package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingShortDto {

    private Integer id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Integer itemId;

    private Integer bookerId;
}
