package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentShortDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @Validated(Create.class)
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto,
                                              @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. post. /items. create item request");
        return new ResponseEntity<>(itemService.createItem(itemDto, userId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> findAll(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                 @RequestParam(defaultValue = "0") @Valid @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = "10") @Valid @Positive int size) {
        log.info("controller. get. /items. find all items request");
        return new ResponseEntity<>(itemService.findAll(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> findById(@PathVariable int id,
                                            @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. get. /items/{}. find item by id request", id);
        return new ResponseEntity<>(itemService.findById(id, userId), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @Validated(Update.class)
    public ResponseEntity<ItemDto> update(@PathVariable int id,
                                          @RequestBody ItemDto itemDto,
                                          @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. patch. /items/{}. update item request", id);
        return new ResponseEntity<>(itemService.update(id, itemDto, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteById(@PathVariable int id) {
        log.info("controller. delete. /items/{}. delete item by id request", id);
        return new ResponseEntity<>(itemService.deleteById(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestParam String text,
                                                @RequestParam(defaultValue = "0") @Valid @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Valid @Positive int size) {
        log.info("controller. get. /search. find items by text={} request", text);
        return new ResponseEntity<>(itemService.search(text, from, size), HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    @Validated(Create.class)
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentShortDto commentShortDto,
                                                    @PathVariable int itemId,
                                                    @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. post. /items. create item request");
        return new ResponseEntity<>(itemService.createComment(commentShortDto, itemId, userId), HttpStatus.OK);
    }
}
