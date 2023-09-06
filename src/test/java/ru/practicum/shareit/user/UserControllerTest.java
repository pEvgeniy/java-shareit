package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.model.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private UserController userController;

    private UserDto userDto;

    private UserDto createdUser;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .name("name")
                .email("user@mail.com")
                .build();
        createdUser = userController.create(userDto);
    }

    @Test
    void create() {
        UserDto foundUser = userController.findById(createdUser.getId());

        Assertions.assertEquals(createdUser.getId(), foundUser.getId());
        Assertions.assertEquals(createdUser.getName(), foundUser.getName());
        Assertions.assertEquals(createdUser.getEmail(), foundUser.getEmail());
    }

    @Test
    void findAll() {
        List<UserDto> foundUsers = userController.findAll();

        Assertions.assertEquals(foundUsers.size(), 1);
        Assertions.assertEquals(foundUsers.get(0).getId(), createdUser.getId());
    }

    @Test
    void findById() {
        UserDto foundUser = userController.findById(createdUser.getId());

        Assertions.assertEquals(createdUser.getId(), foundUser.getId());
        Assertions.assertEquals(createdUser.getName(), foundUser.getName());
        Assertions.assertEquals(createdUser.getEmail(), foundUser.getEmail());
    }

    @Test
    void update() {
        UserDto updatedUser = userController.update(createdUser.getId(),
                UserDto.builder()
                        .id(createdUser.getId())
                        .name("updated name")
                        .email("updated@mail.com")
                        .build());

        Assertions.assertEquals(updatedUser.getId(), createdUser.getId());
        Assertions.assertEquals(updatedUser.getName(), "updated name");
        Assertions.assertEquals(updatedUser.getEmail(), "updated@mail.com");
    }

    @Test
    void deleteById() {
        userController.deleteById(createdUser.getId());

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userController.findById(createdUser.getId())
        );
    }
}