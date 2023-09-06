package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        user = makeUser();
        userDto = makeUserDto();
    }

    @Test
    public void createUserTest() {
        Mockito
                .when(userRepository.save(user))
                .thenReturn(user);
        Mockito
                .when(userMapper.toUser(userDto))
                .thenReturn(user);
        Mockito
                .when(userMapper.toUserDto(user))
                .thenReturn(userDto);

        UserDto createdUserDto = userService.create(userDto);

        Assertions.assertEquals(createdUserDto.getId(), 1);
        Assertions.assertEquals(createdUserDto.getName(), userDto.getName());
        Assertions.assertEquals(createdUserDto.getEmail(), userDto.getEmail());
        Mockito
                .verify(userRepository).save(user);
    }

    @Test
    void findAll() {
        Mockito
                .when(userRepository.findAll())
                .thenReturn(List.of(user));
        Mockito
                .when(userMapper.toUserDto(user))
                .thenReturn(userDto);

        List<UserDto> allUsers = userService.findAll();

        Assertions.assertEquals(allUsers.size(), 1);
        Assertions.assertEquals(allUsers.get(0).getName(), user.getName());
        Mockito
                .verify(userRepository).findAll();
    }

    @Test
    void findById() {
        Mockito
                .when(userRepository.findById(1))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(userMapper.toUserDto(user))
                .thenReturn(userDto);

        UserDto foundUserDto = userService.findById(1);

        Assertions.assertEquals(foundUserDto.getId(), user.getId());
        Assertions.assertEquals(foundUserDto.getName(), user.getName());
        Assertions.assertEquals(foundUserDto.getEmail(), user.getEmail());
        Mockito
                .verify(userRepository).findById(1);
    }

    @Test
    void update() {
        user.setEmail("update@mail.com");
        Mockito
                .when(userRepository.findById(1))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(userMapper.toUserDto(user))
                .thenReturn(userDto);

        UserDto updatedUser = userService.update(1, userDto);

        Assertions.assertEquals(updatedUser.getId(), user.getId());
        Assertions.assertEquals(updatedUser.getName(), user.getName());
        Assertions.assertEquals(updatedUser.getEmail(), user.getEmail());
        Mockito
                .verify(userRepository).findById(1);
    }

    @Test
    void deleteById() {
        Mockito
                .when(userRepository.findById(1))
                .thenReturn(Optional.ofNullable(user));

        int id = userService.deleteById(1);
        Assertions.assertEquals(id, 1);
        Mockito
                .verify(userRepository).deleteById(1);
    }

    private UserDto makeUserDto() {
        return UserDto.builder()
                .id(1)
                .name("user name")
                .email("user@mail.com")
                .build();
    }

    private User makeUser() {
        return User.builder()
                .id(1)
                .name("user name")
                .email("user@mail.com")
                .build();
    }
}
