package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @Validated(Create.class)
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto userDto) {
        log.info("controller. post. /users. create user request");
        return new ResponseEntity<>(userService.create(userDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        log.info("controller. get. /users. find all users");
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable int id) {
        log.info("controller. get. /users/{}. find user by id request", id);
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @Validated(Update.class)
    public ResponseEntity<UserDto> update(@PathVariable int id,
                                          @RequestBody UserDto userDto) {
        log.info("controller. patch. /users/{}. update user by id request", id);
        return new ResponseEntity<>(userService.update(id, userDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteById(@PathVariable int id) {
        log.info("controller. delete. /users/{}. delete user by id request", id);
        return new ResponseEntity<>(userService.deleteById(id), HttpStatus.OK);
    }
}
