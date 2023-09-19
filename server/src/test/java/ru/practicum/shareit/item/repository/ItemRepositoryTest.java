package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User user;

    private Item item;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("user name")
                .email("user@mail.com")
                .build();
        userRepository.save(user);
        item = Item.builder()
                .name("item name")
                .description("description")
                .available(true)
                .owner(user)
                .build();
        itemRepository.save(item);
    }

    @Test
    void findByOwnerId() {
        List<Item> foundItems = itemRepository.findByOwnerIdOrderByIdAsc(1, PageRequest.of(0, 10));
        assertEquals(1, foundItems.size());
    }

    @Test
    void findByRequestId() {
        ItemRequest itemRequest = ItemRequest.builder()
                .description("description")
                .requester(user)
                .created(LocalDateTime.now())
                .build();
        itemRequestRepository.save(itemRequest);
        item.setRequest(itemRequest);
        itemRepository.save(item);
        List<Item> foundItems = itemRepository.findByRequestId(itemRequest.getId());
        assertEquals(1, foundItems.size());
    }

    @Test
    void searchAvailableItemsByText() {
        List<Item> foundItems = itemRepository.searchAvailableItemsByText("item name", PageRequest.of(0, 10));
        assertEquals(1, foundItems.size());
    }
}