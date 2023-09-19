package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.dto.ItemDtoForRequests;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestMapper itemRequestMapper;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private User user;

    private Item item;

    private ItemRequest itemRequest;

    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        user = makeUser();
        item = makeItem(user);
        itemRequest = makeItemRequest(user);
        itemRequestDto = makeItemRequestDto();
    }

    @Test
    void create() {
        Mockito
                .when(userRepository.findById(1))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(itemRequestMapper.toItemRequest(any(ItemRequestDto.class)))
                .thenReturn(itemRequest);
        Mockito
                .when(itemRequestMapper.toItemRequestDto(any(ItemRequest.class)))
                .thenReturn(itemRequestDto);
        Mockito
                .when(itemRequestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);

        ItemRequestDto createdItemRequest = itemRequestService.create(makeItemRequestDto(), user.getId());

        assertEquals(itemRequestDto.getDescription(), createdItemRequest.getDescription());
    }

    @Test
    void findAllByRequester() {
        Mockito
                .when(userRepository.findById(1))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(itemRequestRepository.findAllByRequesterId(any(Integer.class)))
                .thenReturn(List.of(itemRequest));
        Mockito
                .when(itemRequestMapper.toItemRequestDto(any(ItemRequest.class)))
                .thenReturn(itemRequestDto);
        Mockito
                .when(itemRepository.findByRequestId(any(Integer.class)))
                .thenReturn(List.of(item));
        Mockito
                .when(itemMapper.toItemDtoForRequests(any(Item.class)))
                .thenReturn(makeItemDtoForRequests(user.getId()));

        List<ItemRequestDto> foundItemRequests = itemRequestService.findAllByRequester(1);

        assertEquals(1, foundItemRequests.size());
    }

    @Test
    void findAllFromOthers() {
        Mockito
                .when(itemRequestRepository.findAllByRequesterIdNot(any(PageRequest.class), any(Integer.class)))
                .thenReturn(List.of(itemRequest));
        Mockito
                .when(itemRequestMapper.toItemRequestDto(any(ItemRequest.class)))
                .thenReturn(itemRequestDto);
        Mockito
                .when(itemRepository.findByRequestId(any(Integer.class)))
                .thenReturn(List.of(item));
        Mockito
                .when(itemMapper.toItemDtoForRequests(any(Item.class)))
                .thenReturn(makeItemDtoForRequests(user.getId()));

        List<ItemRequestDto> foundItemRequests = itemRequestService.findAllFromOthers(1, 0, 10);

        assertEquals(1, foundItemRequests.size());
    }

    @Test
    void findById() {
        Mockito
                .when(userRepository.findById(1))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(itemRequestRepository.findById(1))
                .thenReturn(Optional.ofNullable(itemRequest));
        Mockito
                .when(itemRequestMapper.toItemRequestDto(any(ItemRequest.class)))
                .thenReturn(itemRequestDto);
        Mockito
                .when(itemRepository.findByRequestId(any(Integer.class)))
                .thenReturn(List.of(item));
        Mockito
                .when(itemMapper.toItemDtoForRequests(any(Item.class)))
                .thenReturn(makeItemDtoForRequests(user.getId()));

        ItemRequestDto foundItemRequest = itemRequestService.findById(1, 1);

        assertEquals(itemRequestDto.getDescription(), foundItemRequest.getDescription());
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

    private ItemRequest makeItemRequest(User requester) {
        return ItemRequest.builder()
                .id(1)
                .description("request description")
                .created(LocalDateTime.now())
                .requester(requester)
                .build();
    }

    private ItemRequestDto makeItemRequestDto() {
        return ItemRequestDto.builder()
                .id(1)
                .description("request description")
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();
    }

    private ItemDtoForRequests makeItemDtoForRequests(Integer requesterId) {
        return ItemDtoForRequests.builder()
                .id(1)
                .name("item name")
                .description("item description")
                .available(true)
                .requestId(requesterId)
                .build();
    }
}