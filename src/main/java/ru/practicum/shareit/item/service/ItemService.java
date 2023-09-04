package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentShortDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, int userId);

    List<ItemDto> findAll(int userId, int from, int size);

    ItemDto findById(int id, int userId);

    ItemDto update(int id, ItemDto itemDto, int userId);

    int deleteById(int id);

    List<ItemDto> search(String text, int from, int size);

    CommentDto createComment(CommentShortDto commentShortDto, int itemId, int userId);
}
