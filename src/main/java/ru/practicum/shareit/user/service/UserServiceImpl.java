package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserStorage userStorage;

    @Override
    public UserDto create(UserDto userDto) {
        return userMapper.toUserDto(
                userStorage.create(userMapper.toUser(userDto))
        );
    }

    @Override
    public List<UserDto> findAll() {
        return userStorage.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(int id) {
        return userMapper.toUserDto(userStorage.findById(id));
    }

    @Override
    public UserDto update(int id, UserDto userDto) {
        return userMapper.toUserDto(
                userStorage.update(id, userMapper.toUser(userDto))
        );
    }

    @Override
    public int deleteById(int id) {
        return userStorage.deleteById(id);
    }
}
