package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.exception.model.EntityNotFoundException;
import ru.practicum.shareit.exception.model.ItemUnavailableException;
import ru.practicum.shareit.exception.model.WrongParamException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentShortDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemControllerTest {

    @Autowired
    private ItemController itemController;

    @Autowired
    private UserController userController;

    @Autowired
    private BookingController bookingController;

    private ItemDto createdItem;

    @BeforeEach
    void setUp() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("user name")
                .email("user@mail.com")
                .build();
        userController.create(userDto);

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("item name")
                .description("item description")
                .available(true)
                .requestId(null)
                .comments(Collections.emptyList())
                .build();
        createdItem = itemController.createItem(itemDto, 1);
    }

    void assertItemDtoFields(ItemDto itemToBeAsserted) {
        assertEquals(itemToBeAsserted.getId(), createdItem.getId());
        assertEquals(itemToBeAsserted.getName(), createdItem.getName());
        assertEquals(itemToBeAsserted.getDescription(), createdItem.getDescription());
        assertEquals(itemToBeAsserted.getAvailable(), createdItem.getAvailable());
        assertEquals(itemToBeAsserted.getComments().size(), 0);
        assertNull(itemToBeAsserted.getNextBooking());
    }

    void assertItemDtoFields(ItemDto itemToBeAsserted, String newName) {
        assertEquals(itemToBeAsserted.getId(), createdItem.getId());
        assertEquals(itemToBeAsserted.getName(), newName);
        assertEquals(itemToBeAsserted.getDescription(), createdItem.getDescription());
        assertEquals(itemToBeAsserted.getAvailable(), createdItem.getAvailable());
        assertNull(itemToBeAsserted.getComments());
        assertNull(itemToBeAsserted.getNextBooking());
    }

    @Test
    void createItem() {
        ItemDto foundItem = itemController.findById(createdItem.getId(), 1);
        assertItemDtoFields(foundItem);
    }

    @Test
    void createItemWhenUserNotExists() {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("item name")
                .description("item description")
                .available(true)
                .requestId(null)
                .comments(Collections.emptyList())
                .build();
        assertThrows(
                EntityNotFoundException.class,
                () -> itemController.createItem(itemDto, 99)
        );
    }

    @Test
    void createItemWhenRequestNotExists() {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("item name")
                .description("item description")
                .available(true)
                .requestId(99)
                .comments(Collections.emptyList())
                .build();
        assertThrows(
                EntityNotFoundException.class,
                () -> itemController.createItem(itemDto, 1)
        );
    }

    @Test
    void findAll() {
        List<ItemDto> foundItems = itemController.findAll(1, 0, 20);
        assertEquals(foundItems.size(), 1);
        assertItemDtoFields(foundItems.get(0));
    }

    @Test
    void findAllWithBookings() throws InterruptedException {
        UserDto userDto = UserDto.builder()
                .name("user2 name")
                .email("user2@mail.com")
                .build();
        userController.create(userDto);
        BookingShortDto bookingLast = BookingShortDto.builder()
                .start(LocalDateTime.now().plusSeconds(1))
                .end(LocalDateTime.now().plusSeconds(2))
                .itemId(1)
                .bookerId(2)
                .build();
        bookingController.create(bookingLast, 2);
        bookingController.update(true, 1, 1);
        Thread.sleep(1000);
        BookingShortDto bookingNext = BookingShortDto.builder()
                .start(LocalDateTime.now().plusMinutes(3))
                .end(LocalDateTime.now().plusMinutes(10))
                .itemId(1)
                .bookerId(2)
                .build();
        bookingController.create(bookingNext, 2);
        bookingController.update(true, 2, 1);
        List<ItemDto> foundItems = itemController.findAll(1, 0, 20);
        assertEquals(1, foundItems.size());
        assertNotNull(foundItems.get(0).getLastBooking());
        assertNotNull(foundItems.get(0).getNextBooking());
    }

    @Test
    void findAllWhenUserNotExists() {
        assertThrows(
                EntityNotFoundException.class,
                () -> itemController.findAll(99, 0, 10)
        );
    }

    @Test
    void findById() {
        ItemDto foundItem = itemController.findById(1, 1);
        assertItemDtoFields(foundItem);
    }

    @Test
    void findByIdWhenUserNotExists() {
        assertThrows(
                EntityNotFoundException.class,
                () -> itemController.findById(1, 99)
        );
    }

    @Test
    void findByIdWhenItemNotExists() {
        assertThrows(
                EntityNotFoundException.class,
                () -> itemController.findById(99, 1)
        );
    }

    @Test
    void updateName() {
        createdItem.setName("new name");
        ItemDto updatedItem = itemController.update(1, createdItem, 1);
        assertItemDtoFields(updatedItem, "new name");
    }

    @Test
    void updateWhenNothingToUpdate() {
        ItemDto updatedItem = itemController.update(1, createdItem, 1);
        assertItemDtoFields(updatedItem, "item name");
    }

    @Test
    void updateWhenUserNotExists() {
        assertThrows(
                EntityNotFoundException.class,
                () -> itemController.update(1, createdItem, 99)
        );
    }

    @Test
    void updateWhenItemNotExists() {
        assertThrows(
                EntityNotFoundException.class,
                () -> itemController.update(99, createdItem, 1)
        );
    }

    @Test
    void updateWhenItemOwnerIdNotEqualsUserId() {
        UserDto userDto = UserDto.builder()
                .id(2)
                .name("user2 name")
                .email("user2@mail.com")
                .build();
        userController.create(userDto);
        assertThrows(
                WrongParamException.class,
                () -> itemController.update(1, createdItem, 2)
        );
    }

    @Test
    void deleteById() {
        itemController.deleteById(1);
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> itemController.findById(1, 1)
        );
    }

    @Test
    void deleteByIdWhenItemNotExists() {
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> itemController.deleteById(99)
        );
    }

    @Test
    void search() {
        List<ItemDto> foundItems = itemController.search("name", 1, 10);
        assertEquals(foundItems.size(), 1);
        assertEquals(foundItems.get(0).getId(), createdItem.getId());
        assertEquals(foundItems.get(0).getName(), createdItem.getName());
        assertEquals(foundItems.get(0).getDescription(), createdItem.getDescription());
        assertEquals(foundItems.get(0).getAvailable(), createdItem.getAvailable());
        assertNull(foundItems.get(0).getComments());
        assertNull(foundItems.get(0).getNextBooking());
    }

    @Test
    void searchWhenTextIsBlank() {
        List<ItemDto> foundItems = itemController.search("  ", 1, 10);
        assertEquals(0, foundItems.size());
    }

    @Test
    void searchWhenTextIsNull() {
        List<ItemDto> foundItems = itemController.search(null, 1, 10);
        assertEquals(0, foundItems.size());
    }

    @Test
    void createComment() {
        Assertions.assertThrows(
                ItemUnavailableException.class,
                () -> itemController.createComment(
                        CommentShortDto.builder()
                                .text("my first comment")
                                .build(), 1, 1)
        );
    }

    @Test
    void createCommentWithBookings() throws InterruptedException {
        UserDto userDto = UserDto.builder()
                .name("user2 name")
                .email("user2@mail.com")
                .build();
        userController.create(userDto);
        BookingShortDto bookingLast = BookingShortDto.builder()
                .start(LocalDateTime.now().plusSeconds(1))
                .end(LocalDateTime.now().plusSeconds(2))
                .itemId(1)
                .bookerId(2)
                .build();
        bookingController.create(bookingLast, 2);
        bookingController.update(true, 1, 1);
        Thread.sleep(3000);
        BookingShortDto bookingNext = BookingShortDto.builder()
                .start(LocalDateTime.now().plusMinutes(3))
                .end(LocalDateTime.now().plusMinutes(10))
                .itemId(1)
                .bookerId(2)
                .build();
        bookingController.create(bookingNext, 2);
        bookingController.update(true, 2, 1);
        CommentDto comment = itemController.createComment(
                CommentShortDto.builder()
                        .text("my first comment")
                        .build(), 1, 2);

        assertEquals(comment.getText(), "my first comment");
    }

    @Test
    void createCommentWhenUserNotExists() {
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> itemController.createComment(
                        CommentShortDto.builder()
                                .text("my first comment")
                                .build(), 1, 99)
        );
    }

    @Test
    void createCommentWhenItemNotExists() {
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> itemController.createComment(
                        CommentShortDto.builder()
                                .text("my first comment")
                                .build(), 99, 1)
        );
    }
}