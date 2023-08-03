package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);

    List<User> findAll();

    User findById(int id);

    User update(int id, User user);

    int deleteById(int id);
}
