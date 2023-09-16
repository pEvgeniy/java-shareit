package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private User owner;

    private User booker;

    private Item item;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .name("owner name")
                .email("owner@mail.com")
                .build();
        userRepository.save(owner);
        item = Item.builder()
                .name("item name")
                .description("description")
                .available(true)
                .owner(owner)
                .build();
        itemRepository.save(item);
        booker = User.builder()
                .name("booker name")
                .email("booker@mail.com")
                .build();
        userRepository.save(booker);
        Booking booking = Booking.builder()
                .id(1)
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusMinutes(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking);
    }

    @Test
    void findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore() {
        List<Booking> foundBookings = bookingRepository.findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(
                booker.getId(), item.getId(), BookingStatus.WAITING, LocalDateTime.now().plusHours(1));
        assertEquals(1, foundBookings.size());
    }

    @Test
    void findBookingByBookerIdOrderByStartDesc() {
        List<Booking> foundBookings = bookingRepository.findBookingByBookerIdOrderByStartDesc(
                booker.getId(), PageRequest.of(0, 10));
        assertEquals(1, foundBookings.size());
    }

    @Test
    void findBookingByItemOwnerIdOrderByStartDesc() {
        List<Booking> foundBookings = bookingRepository.findBookingByItemOwnerIdOrderByStartDesc(
                owner.getId(), PageRequest.of(0, 10));
        assertEquals(1, foundBookings.size());
    }

    @Test
    void findAllByItemIdAndStatusOrderByStartAsc() {
        List<Booking> foundBookings = bookingRepository.findAllByItemIdAndStatusOrderByStartAsc(
                item.getId(), BookingStatus.WAITING);
        assertEquals(1, foundBookings.size());
    }

    @Test
    void findAllByItemIdAndStatusOrderByStartDesc() {
        List<Booking> foundBookings = bookingRepository.findAllByItemIdAndStatusOrderByStartDesc(
                item.getId(), BookingStatus.WAITING);
        assertEquals(1, foundBookings.size());
    }
}