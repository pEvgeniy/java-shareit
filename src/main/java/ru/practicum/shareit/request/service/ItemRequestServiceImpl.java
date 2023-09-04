package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.EntityNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestMapper itemRequestMapper;

    private final ItemMapper itemMapper;

    private final ItemRequestRepository itemRequestRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto create(ItemRequestDto itemRequestDto, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. user with id = %s not found", userId)));
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(user);
        itemRequestRepository.save(itemRequest);
        return itemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> findAllByRequester(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. user with id = %s not found", userId)));
        List<ItemRequestDto> itemRequests = itemRequestRepository.findAllByRequesterId(user.getId())
                .stream()
                .map(itemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        itemRequests.forEach(i -> i.setItems(itemRepository.findByRequestId(i.getId())
                .stream()
                .map(itemMapper::toItemDtoForRequests)
                .collect(Collectors.toList())));
        return itemRequests;
    }

    @Override
    public List<ItemRequestDto> findAllFromOthers(int userId, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "created"));

        List<ItemRequestDto> itemRequests = itemRequestRepository.findAllByRequesterIdNot(pageRequest, userId)
                .stream()
                .map(itemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        itemRequests.forEach(i -> i.setItems(itemRepository.findByRequestId(i.getId())
                .stream()
                .map(itemMapper::toItemDtoForRequests)
                .collect(Collectors.toList())));
        return itemRequests;
    }

    @Override
    public ItemRequestDto findById(int itemRequestId, int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. user with id = %s not found", userId)));
        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. item request with id = %s not found", itemRequestId)));
        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(itemRepository.findByRequestId(itemRequestDto.getId())
                .stream()
                .map(itemMapper::toItemDtoForRequests)
                .collect(Collectors.toList()));
        return itemRequestDto;
    }
}
