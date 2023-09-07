package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.model.EntityNotFoundException;
import ru.practicum.shareit.exception.model.ItemUnavailableException;
import ru.practicum.shareit.item.dto.CommentShortDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemControllerTest {

    @Autowired
    private ItemController itemController;

    @Autowired
    private UserController userController;

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
    void findAll() {
        List<ItemDto> foundItems = itemController.findAll(1, 0, 20);
        assertEquals(foundItems.size(), 1);
        assertItemDtoFields(foundItems.get(0));
    }

    @Test
    void findById() {
        ItemDto foundItem = itemController.findById(1, 1);
        assertItemDtoFields(foundItem);
    }

    @Test
    void update() {
        createdItem.setName("new name");
        ItemDto updatedItem = itemController.update(1, createdItem, 1);
        assertItemDtoFields(updatedItem, "new name");
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
    void createComment() {
        Assertions.assertThrows(
                ItemUnavailableException.class,
                () -> itemController.createComment(
                        CommentShortDto.builder()
                                .text("my first comment")
                                .build(), 1, 1)
        );
    }
}