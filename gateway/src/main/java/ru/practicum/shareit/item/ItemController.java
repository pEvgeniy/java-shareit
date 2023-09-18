package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestClientDto;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Create.class)
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemRequestClientDto itemRequestDto,
                                             @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. post. /items. create item request");
        return itemClient.create(itemRequestDto, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAll(@RequestHeader(X_SHARER_USER_ID) int userId,
                                 @RequestParam(defaultValue = "0") @Valid @PositiveOrZero int from,
                                 @RequestParam(defaultValue = "10") @Valid @Positive int size) {
        log.info("controller. get. /items. find all items request");
        return itemClient.findAll(userId, from, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findById(@PathVariable int id,
                            @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. get. /items/{}. find item by id request", id);
        return itemClient.findById(id, userId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Validated(Update.class)
    public ResponseEntity<Object> update(@PathVariable int id,
                          @RequestBody ItemRequestClientDto itemRequestDto,
                          @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. patch. /items/{}. update item request", id);
        return itemClient.update(id, userId, itemRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> deleteById(@PathVariable int id) {
        log.info("controller. delete. /items/{}. delete item by id request", id);
        return itemClient.deleteById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> search(@RequestParam String text,
                                @RequestParam(defaultValue = "0") @Valid @PositiveOrZero int from,
                                @RequestParam(defaultValue = "10") @Valid @Positive int size) {
        log.info("controller. get. /search. find items by text={} request", text);
        return itemClient.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    @Validated(Create.class)
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentRequestDto commentRequestDto,
                                    @PathVariable int itemId,
                                    @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. post. /items. create item request");
        return itemClient.createComment(commentRequestDto, itemId, userId);
    }
}
