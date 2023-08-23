package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

public interface BookingService {

    BookingDto create(BookingShortDto bookingShortDto, int userId);

    List<BookingDto> findAllByBooker(String state, int userId);

    List<BookingDto> findAllByOwner(String state, int userId);

    BookingDto findById(int id, int userId);

    BookingDto update(boolean isApproved, int id, int userId);

    int deleteById(int id);

}
