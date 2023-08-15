package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Item item, Integer userId);

    List<Item> findAll(int userId);

    Item findById(int id);

    Item update(int id, Item item, int userId);

    int deleteById(int id);

    List<Item> search(String text);
}
