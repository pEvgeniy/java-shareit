package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User user;

    private Item item;

    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("user name")
                .email("user@mail.com")
                .build();
        userRepository.save(user);
        itemRequest = ItemRequest.builder()
                .id(1)
                .description("request description")
                .created(LocalDateTime.now())
                .requester(user)
                .build();
        itemRequestRepository.save(itemRequest);
        item = Item.builder()
                .name("item name")
                .description("description")
                .available(true)
                .owner(user)
                .request(itemRequest)
                .build();
        itemRepository.save(item);
    }

    @Test
    void findAllByRequesterId() {
        List<ItemRequest> foundItemRequests = itemRequestRepository.findAllByRequesterId(user.getId());
        assertEquals(1, foundItemRequests.size());
    }

    @Test
    void findAllByRequesterIdNot() {
        List<ItemRequest> foundItemRequests = itemRequestRepository.findAllByRequesterIdNot(PageRequest.of(0, 10), 1);
        assertEquals(0, foundItemRequests.size());
    }
}