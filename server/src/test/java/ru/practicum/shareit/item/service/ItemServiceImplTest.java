package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentShortDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private User user;

    private Item item;

    private ItemDto itemDto;

    private ItemRequest itemRequest;

    private Booking booking;

    @BeforeEach
    void setUp() {
        user = makeUser();
        item = makeItem(user);
        itemDto = makeItemDto();
        itemRequest = makeItemRequest(user);
        booking = makeBooking(item, user);
    }

    @Test
    void createItem() {
        Mockito
                .when(userRepository.findById(1))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(itemMapper.toItem(itemDto))
                .thenReturn(item);
        Mockito
                .when(itemMapper.toItemDto(item))
                .thenReturn(itemDto);
        Mockito
                .when(itemRequestRepository.findById(itemDto.getRequestId()))
                .thenReturn(Optional.ofNullable(itemRequest));
        Mockito
                .when(itemRepository.save(item))
                .thenReturn(item);

        ItemDto createdItem = itemService.createItem(itemDto, 1);

        assertItemDtoFields(createdItem);
        assertNull(createdItem.getLastBooking());
    }

    @Test
    void findAll() {
        Mockito
                .when(userRepository.findById(1))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(itemRepository.findByOwnerIdOrderByIdAsc(1, PageRequest.of(0, 10)))
                .thenReturn(List.of(item));
        Mockito
                .when(itemMapper.toItemDto(item))
                .thenReturn(itemDto);
        setUpBookingToItemsMocks();
        setUpCommentsToItemMocks();

        List<ItemDto> items = itemService.findAll(1, 0, 10);

        assertEquals(items.size(), 1);
        assertItemDtoFields(items.get(0));
        assertEquals(items.get(0).getLastBooking().getId(), 1);
    }

    @Test
    void findById() {
        Mockito
                .when(userRepository.findById(1))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(itemRepository.findById(1))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(itemMapper.toItemDto(item))
                .thenReturn(itemDto);
        setUpBookingToItemsMocks();
        setUpCommentsToItemMocks();

        ItemDto foundItem = itemService.findById(1, 1);

        assertItemDtoFields(foundItem);
        assertEquals(foundItem.getLastBooking().getId(), 1);
    }

    @Test
    void update() {
        Mockito
                .when(userRepository.findById(1))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(itemRepository.findById(1))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(itemMapper.toItem(itemDto))
                .thenReturn(item);
        Mockito
                .when(itemMapper.toItemDto(item))
                .thenReturn(itemDto);

        ItemDto updatedItem = itemService.update(1, itemDto, 1);
        assertItemDtoFields(updatedItem);
        assertNull(updatedItem.getLastBooking());
    }

    @Test
    void search() {
        Mockito
                .when(itemRepository.searchAvailableItemsByText(any(String.class), any(PageRequest.class)))
                .thenReturn(List.of(item));
        Mockito
                .when(itemMapper.toItemDto(item))
                .thenReturn(itemDto);

        List<ItemDto> items = itemService.search("text", 1, 20);

        assertItemDtoFields(items.get(0));
    }

    @Test
    void createComment() {
        Mockito
                .when(userRepository.findById(1))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(itemRepository.findById(1))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(bookingRepository.findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(
                        any(Integer.class), any(Integer.class), any(BookingStatus.class), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        Mockito
                .when(commentMapper.toComment(any(CommentShortDto.class)))
                .thenReturn(makeComment(user, item));
        Mockito
                .when(commentMapper.toCommentDto(any(Comment.class)))
                .thenReturn(makeCommentDto());
        Mockito
                .when(commentRepository.save(any(Comment.class)))
                .thenReturn(makeComment(user, item));

        CommentDto createdComment = itemService.createComment(makeCommentShortDto(), 1, 1);

        assertEquals(createdComment.getId(), 1);
        assertEquals(createdComment.getAuthorName(), "author");
        assertEquals(createdComment.getText(), "my first comment");
    }

    void setUpBookingToItemsMocks() {
        Mockito
                .when(bookingRepository.findAllByItemIdAndStatusOrderByStartDesc(1, BookingStatus.APPROVED))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingRepository.findAllByItemIdAndStatusOrderByStartAsc(1, BookingStatus.APPROVED))
                .thenReturn(Collections.emptyList());
        Mockito
                .when(bookingMapper.toBookingShortDto(any(Booking.class)))
                .thenReturn(makeBookingShortDto());
    }

    void setUpCommentsToItemMocks() {
        Mockito
                .when(commentRepository.findAllByItemId(1))
                .thenReturn(Collections.emptyList());
    }

    void assertItemDtoFields(ItemDto itemToBeAsserted) {
        assertEquals(itemToBeAsserted.getId(), item.getId());
        assertEquals(itemToBeAsserted.getName(), item.getName());
        assertEquals(itemToBeAsserted.getDescription(), item.getDescription());
        assertEquals(itemToBeAsserted.getAvailable(), item.getAvailable());
        assertEquals(itemToBeAsserted.getComments().size(), 0);
        assertNull(itemToBeAsserted.getNextBooking());
    }

    private User makeUser() {
        return User.builder()
                .id(1)
                .name("user name")
                .email("user@mail.com")
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

    private ItemDto makeItemDto() {
        return ItemDto.builder()
                .id(1)
                .name("item name")
                .description("item description")
                .available(true)
                .requestId(1)
                .comments(Collections.emptyList())
                .build();
    }

    private Booking makeBooking(Item item, User booker) {
        return Booking.builder()
                .id(1)
                .start(LocalDateTime.now().minusMinutes(5))
                .end(LocalDateTime.now().minusMinutes(1))
                .item(item)
                .booker(booker)
                .build();
    }

    private BookingShortDto makeBookingShortDto() {
        return BookingShortDto.builder()
                .id(1)
                .itemId(1)
                .start(LocalDateTime.now().minusMinutes(5))
                .end(LocalDateTime.now().minusMinutes(1))
                .build();
    }

    private ItemRequest makeItemRequest(User requester) {
        return ItemRequest.builder()
                .id(1)
                .description("request description")
                .created(LocalDateTime.now())
                .requester(requester)
                .build();
    }

    private Comment makeComment(User author, Item item) {
        return Comment.builder()
                .id(1)
                .author(author)
                .text("my first comment")
                .item(item)
                .created(LocalDateTime.now())
                .build();
    }

    private CommentDto makeCommentDto() {
        return CommentDto.builder()
                .id(1)
                .authorName("author")
                .text("my first comment")
                .created(LocalDateTime.now())
                .build();
    }

    private CommentShortDto makeCommentShortDto() {
        return CommentShortDto.builder()
                .text("my first comment")
                .build();
    }
}