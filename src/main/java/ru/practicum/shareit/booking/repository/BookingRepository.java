package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(Integer bookerId,
                                                                          Integer itemId,
                                                                          BookingStatus status,
                                                                          LocalDateTime end);

    List<Booking> findBookingByBookerIdOrderByStartDesc(Integer bookerId, PageRequest pageRequest);

    List<Booking> findBookingByItemOwnerIdOrderByStartDesc(Integer ownerId, PageRequest pageRequest);

    List<Booking> findAllByItemIdAndStatusOrderByStartAsc(Integer itemId, BookingStatus status);

    List<Booking> findAllByItemIdAndStatusOrderByStartDesc(Integer itemId, BookingStatus status);

}
