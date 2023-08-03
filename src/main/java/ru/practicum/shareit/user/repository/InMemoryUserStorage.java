package ru.practicum.shareit.user.repository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.model.EmailNotUniqueException;
import ru.practicum.shareit.exception.model.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Component
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class InMemoryUserStorage implements UserStorage {
    Map<Integer, User> users = new HashMap<>();
    static int uniqId;

    @Override
    public User create(User user) {
        emailExistenceValidationOnCreation(user);
        incrementUniqId();
        user.setId(uniqId);
        users.put(uniqId, user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(int id) {
        if (!users.containsKey(id)) {
            throw new EntityNotFoundException(String.format("User with id = %s not found", id));
        }
        return users.get(id);
    }

    @Override
    public User update(int id, User user) {
        user.setId(id);
        User userToBeUpdated = users.get(id);
        if (!users.containsKey(userToBeUpdated.getId())) {
            throw new EntityNotFoundException(String.format("User with id = %s not found", user.getId()));
        }
        if (user.getName() != null) {
            userToBeUpdated.setName(user.getName());
        }
        if (user.getEmail() != null) {
            emailExistenceValidationOnUpdate(user);
            userToBeUpdated.setEmail(user.getEmail());
        }
        users.put(id, userToBeUpdated);
        return userToBeUpdated;
    }

    @Override
    public int deleteById(int id) {
        if (!users.containsKey(id)) {
            throw new EntityNotFoundException(String.format("User with id = %s not found", id));
        }
        users.remove(id);
        return id;
    }

    private void emailExistenceValidationOnUpdate(User user) {
        users.values()
                .stream()
                .filter(u ->  user.getEmail().equals(u.getEmail()) && !user.getId().equals(u.getId()))
                .findFirst()
                .ifPresent(u -> {
                    throw new EmailNotUniqueException(String.format("Email %s already exists", u.getEmail()));
                });
    }

    private void emailExistenceValidationOnCreation(User user) {
        users.values()
                .stream()
                .filter(u ->  user.getEmail().equals(u.getEmail()))
                .findFirst()
                .ifPresent(u -> {
                    throw new EmailNotUniqueException(String.format("Email %s already exists", u.getEmail()));
                });
    }

    private void incrementUniqId() {
        uniqId++;
    }
}
