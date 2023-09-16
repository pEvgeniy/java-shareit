package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingDto toBookingDto(Booking booking);

    @Mapping(target = "itemId", source = "booking.item.id")
    @Mapping(target = "bookerId", source = "booking.booker.id")
    BookingShortDto toBookingShortDto(Booking booking);

    Booking toBooking(BookingShortDto bookingShortDto);

}
