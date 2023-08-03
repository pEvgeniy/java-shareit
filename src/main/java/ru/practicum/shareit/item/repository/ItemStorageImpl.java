package ru.practicum.shareit.item.repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.model.EntityNotFoundException;
import ru.practicum.shareit.exception.model.WrongHeaderParamException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.InMemoryUserStorage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {

    InMemoryUserStorage userStorage;
    Map<Integer, Item> items = new HashMap<>();
    static int uniqId;

    @Override
    public Item create(Item item, Integer userId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new EntityNotFoundException(String.format("User with id = %s not found", userId));
        }
        item.setId(++uniqId);
        item.setRequest(userId);
        items.put(uniqId, item);
        return item;
    }

    @Override
    public List<Item> findAll(int userId) {
        return items.values()
                .stream()
                .filter(i -> i.getRequest() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(int id) {
        if (!items.containsKey(id)) {
            throw new EntityNotFoundException(String.format("Item with id = %s not found", id));
        }
        return items.get(id);
    }

    @Override
    public Item update(int id, Item item, int userId) {
        if (!items.containsKey(id)) {
            throw new EntityNotFoundException(String.format("Item with id = %s not found", item.getId()));
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new EntityNotFoundException(String.format("User with id = %s not found", userId));
        }
        Item oldItemToBeUpdated = items.get(id);
        if (oldItemToBeUpdated.getRequest() != userId) {
            throw new WrongHeaderParamException(String.format("Wrong userId = %s for item with id = %s", userId, id));
        }
        if (item.getName() != null) {
            oldItemToBeUpdated.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItemToBeUpdated.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItemToBeUpdated.setAvailable(item.getAvailable());
        }
        items.put(id, oldItemToBeUpdated);
        return oldItemToBeUpdated;
    }

    @Override
    public int deleteById(int id) {
        if (!items.containsKey(id)) {
            throw new EntityNotFoundException(String.format("Item with id = %s not found", id));
        }
        items.remove(id);
        return id;
    }

    @Override
    public List<Item> search(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return items.values()
                .stream()
                .filter(i -> i.getAvailable() && findTextInItem(i, text.toLowerCase()))
                .collect(Collectors.toList());
    }

    private boolean findTextInItem(Item item, String text) {
        return item.getName().toLowerCase().contains(text)
                || item.getDescription().toLowerCase().contains(text);
    }
}
