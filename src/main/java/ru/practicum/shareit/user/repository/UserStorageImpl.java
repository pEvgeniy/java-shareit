package ru.practicum.shareit.user.repository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.model.EmailNotUniqueException;
import ru.practicum.shareit.exception.model.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Component
public class UserStorageImpl implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    private static int uniqId;

    @Override
    public User create(User user) {
        emailExistenceValidationOnCreation(user);
        incrementUniqId();
        user.setId(uniqId);
        users.put(uniqId, user);
        log.info("repository. created user with id={}", uniqId);
        return user;
    }

    @Override
    public List<User> findAll() {
        log.info("repository. found all users");
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(int id) {
        if (!users.containsKey(id)) {
            throw new EntityNotFoundException(String.format("repository. user with id = %s not found", id));
        }
        log.info("repository. found by id user with id={}", id);
        return users.get(id);
    }

    @Override
    public User update(int id, User user) {
        user.setId(id);
        User userToBeUpdated = users.get(id);
        if (!users.containsKey(userToBeUpdated.getId())) {
            throw new EntityNotFoundException(String.format("repository. user with id = %s not found", user.getId()));
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            userToBeUpdated.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            emailExistenceValidationOnUpdate(user);
            userToBeUpdated.setEmail(user.getEmail());
        }
        users.put(id, userToBeUpdated);
        log.info("repository. updated user with id={}", id);
        return userToBeUpdated;
    }

    @Override
    public int deleteById(int id) {
        if (!users.containsKey(id)) {
            throw new EntityNotFoundException(String.format("repository. user with id = %s not found", id));
        }
        users.remove(id);
        log.info("repository. deleted by id user with id={}", id);
        return id;
    }

    private void emailExistenceValidationOnUpdate(User user) {
        users.values()
                .stream()
                .filter(u ->  user.getEmail().equals(u.getEmail()) && !user.getId().equals(u.getId()))
                .findFirst()
                .ifPresent(u -> {
                    throw new EmailNotUniqueException(String.format("repository. email %s already exists", u.getEmail()));
                });
    }

    private void emailExistenceValidationOnCreation(User user) {
        users.values()
                .stream()
                .filter(u ->  user.getEmail().equals(u.getEmail()))
                .findFirst()
                .ifPresent(u -> {
                    throw new EmailNotUniqueException(String.format("repository. email %s already exists", u.getEmail()));
                });
    }

    private void incrementUniqId() {
        uniqId++;
    }
}
