package ru.practicum.shareit.booking.repository;

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

    List<Booking> findByBookerId(Integer userId);

    List<Booking> findBookingByBooker_IdOrderByStartDesc(Integer bookerId);

    List<Booking> findBookingByItem_Owner_IdOrderByStartDesc(Integer ownerId);

    List<Booking> findAllByItem_IdOrderByStartAsc(Integer itemId);

    List<Booking> findAllByItem_IdOrderByStartDesc(Integer itemId);

}
