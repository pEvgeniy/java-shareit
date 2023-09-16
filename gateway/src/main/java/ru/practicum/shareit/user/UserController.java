package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.Valid;

@Slf4j
@Validated
@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Create.class)
    public ResponseEntity<Object> create(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("controller. post. /users. create user request");
        return userClient.create(userRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAll() {
        log.info("controller. get. /users. find all users");
        return userClient.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findById(@PathVariable int id) {
        log.info("controller. get. /users/{}. find user by id request", id);
        return userClient.findById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Validated(Update.class)
    public ResponseEntity<Object> update(@PathVariable int id,
                          @RequestBody UserRequestDto userRequestDto) {
        log.info("controller. patch. /users/{}. update user by id request", id);
        return userClient.update(id, userRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> deleteById(@PathVariable int id) {
        log.info("controller. delete. /users/{}. delete user by id request", id);
        return userClient.deleteById(id);
    }
}
