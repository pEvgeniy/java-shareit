package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.model.EntityNotFoundException;
import ru.practicum.shareit.exception.model.WrongHeaderParamException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserStorageImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {

    private final UserStorageImpl userStorage;
    private final Map<Integer, Item> items = new HashMap<>();
    private static int uniqId;

    @Override
    public Item create(Item item, Integer userId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new EntityNotFoundException(String.format("repository. user with id = %s not found", userId));
        }
        incrementUniqId();
        item.setId(uniqId);
        item.setRequest(userId);
        items.put(uniqId, item);
        log.info("repository. item with id={} created", uniqId);
        return item;
    }

    @Override
    public List<Item> findAll(int userId) {
        log.info("repository. item for user with id={} found", userId);
        return items.values()
                .stream()
                .filter(i -> i.getRequest() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(int id) {
        if (!items.containsKey(id)) {
            throw new EntityNotFoundException(String.format("repository. item with id = %s not found", id));
        }
        log.info("repository. item with id={} found", id);
        return items.get(id);
    }

    @Override
    public Item update(int id, Item item, int userId) {
        if (!items.containsKey(id)) {
            throw new EntityNotFoundException(String.format("repository. item with id = %s not found", item.getId()));
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new EntityNotFoundException(String.format("repository. user with id = %s not found", userId));
        }
        Item oldItemToBeUpdated = items.get(id);
        if (oldItemToBeUpdated.getRequest() != userId) {
            throw new WrongHeaderParamException(String.format("repository. wrong userId = %s for item with id = %s", userId, id));
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            oldItemToBeUpdated.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            oldItemToBeUpdated.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItemToBeUpdated.setAvailable(item.getAvailable());
        }
        log.info("repository. item with id={} updated", id);
        items.put(id, oldItemToBeUpdated);
        return oldItemToBeUpdated;
    }

    @Override
    public int deleteById(int id) {
        if (!items.containsKey(id)) {
            throw new EntityNotFoundException(String.format("repository. item with id = %s not found", id));
        }
        items.remove(id);
        log.info("repository. item with id={} deleted", id);
        return id;
    }

    @Override
    public List<Item> search(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        log.info("repository. searching for items with text={}", text);
        return items.values()
                .stream()
                .filter(i -> i.getAvailable() && findTextInItem(i, text.toLowerCase()))
                .collect(Collectors.toList());
    }

    private boolean findTextInItem(Item item, String text) {
        return item.getName().toLowerCase().contains(text)
                || item.getDescription().toLowerCase().contains(text);
    }

    private void incrementUniqId() {
        uniqId++;
    }
}
