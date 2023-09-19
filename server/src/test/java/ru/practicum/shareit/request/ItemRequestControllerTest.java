package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestControllerTest {

    @Autowired
    private ItemController itemController;

    @Autowired
    private UserController userController;

    @Autowired
    private ItemRequestController itemRequestController;

    private ItemRequestDto createdRequestDto;

    @BeforeEach
    void setUp() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("user name")
                .email("user@mail.com")
                .build();
        userController.create(userDto);

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("description")
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();
        createdRequestDto = itemRequestController.create(itemRequestDto, 1).getBody();

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("item name")
                .description("item description")
                .available(true)
                .requestId(itemRequestDto.getId())
                .comments(Collections.emptyList())
                .build();
        itemController.createItem(itemDto, 1);
    }

    @Test
    void create() {
        ItemRequestDto foundItemRequest = itemRequestController.findById(createdRequestDto.getId(), 1).getBody();
        assertEquals(1, foundItemRequest.getItems().size());
    }

    @Test
    void findAll() {
        List<ItemRequestDto> foundItemRequests = itemRequestController.findAll(1).getBody();
        assertEquals(1, foundItemRequests.size());
    }

    @Test
    void findAllFromOthers() {
        List<ItemRequestDto> foundItemRequests = itemRequestController.findAllFromOthers(1, 0, 10).getBody();
        assertEquals(0, foundItemRequests.size());
    }

    @Test
    void findById() {
        ItemRequestDto foundItemRequest = itemRequestController.findById(1, 1).getBody();
        assertEquals(createdRequestDto.getDescription(), foundItemRequest.getDescription());
    }
}