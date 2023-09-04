package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(ItemRequestDto itemRequestDto, int userId);

    List<ItemRequestDto> findAllByRequester(int userId);

    List<ItemRequestDto> findAllFromOthers(int userId, int from, int size);

    ItemRequestDto findById(int itemRequestId, int userId);
}
