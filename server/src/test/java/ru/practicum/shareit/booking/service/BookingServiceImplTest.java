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
import ru.practicum.shareit.exception.model.AccessToEntityDeniedException;
import ru.practicum.shareit.exception.model.EntityNotFoundException;
import ru.practicum.shareit.exception.model.ItemUnavailableException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void createWhenUserNotExists() {
        Mockito
                .when(userRepository.findById(any(Integer.class)))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(
                EntityNotFoundException.class,
                () -> bookingService.create(makeBookingShortDto(item.getId()), booker.getId())
        );
    }

    @Test
    void createWhenItemNotExists() {
        Mockito
                .when(userRepository.findById(any(Integer.class)))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(
                EntityNotFoundException.class,
                () -> bookingService.create(makeBookingShortDto(item.getId()), booker.getId())
        );
    }

    @Test
    void createWhenUserIdEqualsOwnerId() {
        Mockito
                .when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.ofNullable(booker));
        Mockito
                .when(itemRepository.findById(any(Integer.class)))
                .thenReturn(Optional.ofNullable(item));
        BookingShortDto bookingShortDto = makeBookingShortDto(item.getId());
        assertThrows(
                AccessToEntityDeniedException.class,
                () -> bookingService.create(bookingShortDto, bookingShortDto.getItemId())
        );
    }

    @Test
    void createWhenItemUnavailable() {
        Mockito
                .when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.ofNullable(booker));
        item.setAvailable(false);
        Mockito
                .when(itemRepository.findById(any(Integer.class)))
                .thenReturn(Optional.ofNullable(item));
        BookingShortDto bookingShortDto = makeBookingShortDto(item.getId());
        assertThrows(
                ItemUnavailableException.class,
                () -> bookingService.create(bookingShortDto, booker.getId())
        );
    }

    @Test
    void findAllByBookerState() {
        Mockito
                .when(userRepository.findById(booker.getId()))
                .thenReturn(Optional.ofNullable(booker));
        Mockito
                .when(bookingRepository.findBookingByBookerIdOrderByStartDesc(any(Integer.class), any(PageRequest.class)))
                .thenReturn(List.of(makeBooking(item, booker)));
        Mockito
                .when(bookingMapper.toBookingDto(any(Booking.class)))
                .thenReturn(makeBookingDto(item, booker));

        List<BookingDto> foundBookingALL = bookingService.findAllByBooker("ALL", booker.getId(), 0, 10);
        List<BookingDto> foundBookingCURRENT = bookingService.findAllByBooker("CURRENT", booker.getId(), 0, 10);
        List<BookingDto> foundBookingWAITING = bookingService.findAllByBooker("WAITING", booker.getId(), 0, 10);
        List<BookingDto> foundBookingPAST = bookingService.findAllByBooker("PAST", booker.getId(), 0, 10);
        List<BookingDto> foundBookingFUTURE = bookingService.findAllByBooker("FUTURE", booker.getId(), 0, 10);
        List<BookingDto> foundBookingREJECTED = bookingService.findAllByBooker("REJECTED", booker.getId(), 0, 10);

        assertEquals(1, foundBookingALL.size());
        assertEquals(1, foundBookingALL.get(0).getItem().getId());
        assertEquals(2, foundBookingALL.get(0).getBooker().getId());

        assertEquals(0, foundBookingCURRENT.size());
        assertEquals(1, foundBookingWAITING.size());
        assertEquals(1, foundBookingPAST.size());
        assertEquals(0, foundBookingFUTURE.size());
        assertEquals(0, foundBookingREJECTED.size());

        assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.findAllByBooker("FAKE", booker.getId(), 0, 10)
        );
    }

    @Test
    void findAllByBookerStateWhenUserNotExists() {
        Mockito
                .when(userRepository.findById(any(Integer.class)))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(
                EntityNotFoundException.class,
                () -> bookingService.findAllByBooker("ALL", booker.getId(), 0, 10)
        );
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
    void findAllByOwnerStateWhenUserNotExists() {
        Mockito
                .when(userRepository.findById(any(Integer.class)))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(
                EntityNotFoundException.class,
                () -> bookingService.findAllByOwner("ALL", booker.getId(), 0, 10)
        );
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
    void findByIdStateWhenUserNotExists() {
        Mockito
                .when(userRepository.findById(any(Integer.class)))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(
                EntityNotFoundException.class,
                () -> bookingService.findById(1, 2)
        );
    }

    @Test
    void findByIdStateWhenBookerIdEqualsUserId() {
        Mockito
                .when(userRepository.findById(booker.getId()))
                .thenReturn(Optional.ofNullable(booker));
        Booking booking = makeBooking(item, booker);
        booking.getBooker().setId(1);
        Mockito
                .when(bookingRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(booking));

        assertThrows(
                AccessToEntityDeniedException.class,
                () -> bookingService.findById(1, 2)
        );
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

    @Test
    void updateWhenOwnerIdEqualsUserId() {
        Mockito
                .when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.ofNullable(owner));
        Booking booking = makeBooking(item, booker);
        booking.getBooker().setId(1);
        Mockito
                .when(bookingRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(booking));

        assertThrows(
                AccessToEntityDeniedException.class,
                () -> bookingService.update(true, 1, 2)
        );
    }

    @Test
    void updateWhenStatusAlreadyApproved() {
        Mockito
                .when(userRepository.findById(any(Integer.class)))
                .thenReturn(Optional.ofNullable(owner));
        Booking booking = makeBooking(item, booker);
        booking.setStatus(BookingStatus.APPROVED);
        Mockito
                .when(bookingRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(booking));

        assertThrows(
                ItemUnavailableException.class,
                () -> bookingService.update(true, 1, 1)
        );
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