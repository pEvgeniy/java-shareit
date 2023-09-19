package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    List<UserDto> findAll();

    UserDto findById(int id);

    UserDto update(int id, UserDto userDto);

    int deleteById(int id);
}
