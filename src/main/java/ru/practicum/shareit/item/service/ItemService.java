package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, int userId);

    List<ItemDto> findAll(int userId);

    ItemDto findById(int id);

    ItemDto update(int id, ItemDto itemDto, int userId);

    int deleteById(int id);

    List<ItemDto> search(String text);
}
