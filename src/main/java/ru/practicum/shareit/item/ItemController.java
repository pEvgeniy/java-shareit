package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Create.class)
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. post. /items. create item request");
        return itemService.createItem(itemDto, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> findAll(@RequestHeader(X_SHARER_USER_ID) int userId,
                                 @RequestParam(defaultValue = "0") @Valid @PositiveOrZero int from,
                                 @RequestParam(defaultValue = "10") @Valid @Positive int size) {
        log.info("controller. get. /items. find all items request");
        return itemService.findAll(userId, from, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto findById(@PathVariable int id,
                            @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. get. /items/{}. find item by id request", id);
        return itemService.findById(id, userId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Validated(Update.class)
    public ItemDto update(@PathVariable int id,
                          @RequestBody ItemDto itemDto,
                          @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. patch. /items/{}. update item request", id);
        return itemService.update(id, itemDto, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public int deleteById(@PathVariable int id) {
        log.info("controller. delete. /items/{}. delete item by id request", id);
        return itemService.deleteById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> search(@RequestParam String text,
                                @RequestParam(defaultValue = "0") @Valid @PositiveOrZero int from,
                                @RequestParam(defaultValue = "10") @Valid @Positive int size) {
        log.info("controller. get. /search. find items by text={} request", text);
        return itemService.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    @Validated(Create.class)
    public CommentDto createComment(@Valid @RequestBody CommentShortDto commentShortDto,
                                    @PathVariable int itemId,
                                    @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. post. /items. create item request");
        return itemService.createComment(commentShortDto, itemId, userId);
    }
}
