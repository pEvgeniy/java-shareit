package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.model.EntityNotFoundException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingControllerTest {

    @Autowired
    private BookingController bookingController;

    @Autowired
    private ItemController itemController;

    @Autowired
    private UserController userController;

    @BeforeEach
    void setUp() {
        UserDto owner = UserDto.builder()
                .name("user name 1")
                .email("user1@mail.com")
                .build();
        userController.create(owner);

        UserDto booker = UserDto.builder()
                .name("user name 2")
                .email("user2@mail.com")
                .build();
        userController.create(booker);

        ItemDto itemDto = ItemDto.builder()
                .name("item name")
                .description("item description")
                .available(true)
                .build();
        itemController.createItem(itemDto, 1);

        BookingShortDto bookingShortDto = BookingShortDto.builder()
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusMinutes(40))
                .itemId(1)
                .build();
        bookingController.create(bookingShortDto, 2);

    }

    @Test
    void create() {
        BookingDto foundBooking = bookingController.findById(1, 1);

        assertEquals(1, foundBooking.getId());
        assertEquals(2, foundBooking.getBooker().getId());
        assertEquals(1, foundBooking.getItem().getId());
    }

    @Test
    void createWhenUserNotExists() {
        assertThrows(
                EntityNotFoundException.class,
                () -> bookingController.create(makeBookingShortDto(1), 99)
        );
    }

    @Test
    void createWhenItemNotExists() {
        assertThrows(
                EntityNotFoundException.class,
                () -> bookingController.create(makeBookingShortDto(99), 1)
        );
    }

    @Test
    void findAllByBooker() {
        List<BookingDto> foundBooking = bookingController.findAllByBooker("ALL", 2, 0, 10);
        assertEquals(1, foundBooking.size());
    }

    @Test
    void findAllByBookerWhenUserNotExists() {
        assertThrows(
                EntityNotFoundException.class,
                () -> bookingController.findAllByBooker("ALL", 99, 0, 10)
        );
    }

    @Test
    void findAllByOwner() {
        List<BookingDto> foundBooking = bookingController.findAllByOwner("ALL", 1, 0, 10);
        assertEquals(1, foundBooking.size());
    }

    @Test
    void findAllByOwnerWhenUserNotExists() {
        assertThrows(
                EntityNotFoundException.class,
                () -> bookingController.findAllByOwner("ALL", 99, 0, 10)
        );
    }

    @Test
    void findById() {
        BookingDto foundBooking = bookingController.findById(1, 2);
        assertEquals(1, foundBooking.getItem().getId());
        assertEquals(2, foundBooking.getBooker().getId());

    }

    @Test
    void findByIdWhenUserNotExists() {
        assertThrows(
                EntityNotFoundException.class,
                () -> bookingController.findById(1, 99)
        );
    }

    @Test
    void update() {
        BookingDto updatedUser = bookingController.update(true, 1, 1);
        assertEquals(BookingStatus.APPROVED, updatedUser.getStatus());
    }

    @Test
    void updateWhenUserNotExists() {
        assertThrows(
                EntityNotFoundException.class,
                () -> bookingController.update(true, 1, 99)
        );
    }

    @Test
    void updateWhenBookingNotExists() {
        assertThrows(
                EntityNotFoundException.class,
                () -> bookingController.update(true, 99, 1)
        );
    }

    @Test
    void delete() {
        bookingController.delete(1);
        assertThrows(
                EntityNotFoundException.class,
                () -> bookingController.findById(1, 1)
        );
    }

    @Test
    void deleteWhenBookingNotExists() {
        assertThrows(
                EntityNotFoundException.class,
                () -> bookingController.delete(99)
        );
    }

    private BookingShortDto makeBookingShortDto(Integer itemId) {
        return   BookingShortDto.builder()
                .start(LocalDateTime.now().plusMinutes(10))
                .end(LocalDateTime.now().plusMinutes(40))
                .itemId(itemId)
                .build();
    }
}