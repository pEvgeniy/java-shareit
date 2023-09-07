package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    private User owner;

    private User booker;

    private Item item;

    @BeforeEach
    void setUp() {
        owner = makeUser(1, "owner");
        item = makeItem(owner);
        booker = makeUser(2, "booker");
    }

    @Test
    void create() {
        Mockito
                .when(userRepository.findById(booker.getId()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito
                .when(itemRepository.findById(1))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(bookingRepository.save(any(Booking.class)))
                .thenReturn(makeBooking(item, booker));
//        Mockito
//                .when(bookingMapper.toBookingShortDto(any(Booking.class)))
//                .thenReturn(makeBookingShortDto());
        Mockito
                .when(bookingMapper.toBooking(any(BookingShortDto.class)))
                .thenReturn(makeBooking(item, booker));
        Mockito
                .when(bookingMapper.toBookingDto(any(Booking.class)))
                .thenReturn(makeBookingDto(item, booker));

        BookingDto createdBooking = bookingService.create(makeBookingShortDto(item.getId()), booker.getId());

        assertEquals(1, createdBooking.getId());
        assertEquals(1, createdBooking.getItem().getId());
        assertEquals(2, createdBooking.getBooker().getId());
    }

    @Test
    void findAllByBooker() {
        Mockito
                .when(userRepository.findById(booker.getId()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito
                .when(bookingRepository.findBookingByBookerIdOrderByStartDesc(any(Integer.class), any(PageRequest.class)))
                .thenReturn(List.of(makeBooking(item, booker)));
        Mockito
                .when(bookingMapper.toBookingDto(any(Booking.class)))
                .thenReturn(makeBookingDto(item, booker));

        List<BookingDto> foundBooking = bookingService.findAllByBooker("ALL", booker.getId(), 0, 10);

        assertEquals(1, foundBooking.size());
        assertEquals(1, foundBooking.get(0).getItem().getId());
        assertEquals(2, foundBooking.get(0).getBooker().getId());
    }

    @Test
    void findAllByOwner() {
        Mockito
                .when(userRepository.findById(owner.getId()))
                .thenReturn(Optional.ofNullable(owner));
        Mockito
                .when(bookingRepository.findBookingByItemOwnerIdOrderByStartDesc(any(Integer.class), any(PageRequest.class)))
                .thenReturn(List.of(makeBooking(item, booker)));
        Mockito
                .when(bookingMapper.toBookingDto(any(Booking.class)))
                .thenReturn(makeBookingDto(item, booker));

        List<BookingDto> foundBooking = bookingService.findAllByOwner("ALL", owner.getId(), 0, 10);

        assertEquals(1, foundBooking.size());
        assertEquals(1, foundBooking.get(0).getItem().getId());
        assertEquals(2, foundBooking.get(0).getBooker().getId());
    }

    @Test
    void findById() {
        Mockito
                .when(userRepository.findById(booker.getId()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito
                .when(bookingRepository.findById(any(Integer.class)))
                .thenReturn(Optional.ofNullable(makeBooking(item, booker)));
        Mockito
                .when(bookingMapper.toBookingDto(any(Booking.class)))
                .thenReturn(makeBookingDto(item, booker));

        BookingDto foundBooking = bookingService.findById(1, 2);

        assertEquals(1, foundBooking.getId());
        assertEquals(1, foundBooking.getItem().getId());
        assertEquals(2, foundBooking.getBooker().getId());
    }

    @Test
    void update() {
        Mockito
                .when(userRepository.findById(owner.getId()))
                .thenReturn(Optional.ofNullable(owner));
        Mockito
                .when(bookingRepository.findById(any(Integer.class)))
                .thenReturn(Optional.ofNullable(makeBooking(item, booker)));
        Mockito
                .when(bookingMapper.toBookingDto(any(Booking.class)))
                .thenReturn(makeBookingDto(item, booker));

        BookingDto updatedBooking = bookingService.update(true, 1, 1);

        assertEquals(1, updatedBooking.getId());
        assertEquals(1, updatedBooking.getItem().getId());
        assertEquals(2, updatedBooking.getBooker().getId());
    }

    private User makeUser(Integer id, String name) {
        return User.builder()
                .id(id)
                .name(name + " name")
                .email(name + "@mail.com")
                .build();
    }

    private Item makeItem(User owner) {
        return Item.builder()
                .id(1)
                .name("item name")
                .description("item description")
                .available(true)
                .owner(owner)
                .build();
    }

    private Booking makeBooking(Item item, User booker) {
        return Booking.builder()
                .id(1)
                .start(LocalDateTime.now().minusMinutes(5))
                .end(LocalDateTime.now().minusMinutes(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
    }

    private BookingDto makeBookingDto(Item item, User booker) {
        return BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now().minusMinutes(5))
                .end(LocalDateTime.now().minusMinutes(1))
                .item(item)
                .booker(booker)
                .build();
    }

    private BookingShortDto makeBookingShortDto(Integer itemId) {
        return BookingShortDto.builder()
                .id(1)
                .itemId(itemId)
                .start(LocalDateTime.now().minusMinutes(5))
                .end(LocalDateTime.now().minusMinutes(1))
                .build();
    }
}