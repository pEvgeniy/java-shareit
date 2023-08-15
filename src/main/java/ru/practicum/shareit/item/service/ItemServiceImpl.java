package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;

    private final ItemStorage itemStorage;

    @Override
    public ItemDto create(ItemDto itemDto, int userId) {
        return itemMapper.toItemDto(
                itemStorage.create(itemMapper.toItem(itemDto), userId)
        );
    }

    @Override
    public List<ItemDto> findAll(int userId) {
        return itemStorage.findAll(userId)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findById(int id) {
        return itemMapper.toItemDto(itemStorage.findById(id));
    }

    @Override
    public ItemDto update(int id, ItemDto itemDto, int userId) {
        return itemMapper.toItemDto(
                itemStorage.update(id, itemMapper.toItem(itemDto), userId)
        );
    }

    @Override
    public int deleteById(int id) {
        return itemStorage.deleteById(id);
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemStorage.search(text)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
