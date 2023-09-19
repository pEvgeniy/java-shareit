package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.item.ItemController.X_SHARER_USER_ID;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@Valid @RequestBody RequestDto requestDto,
                                         @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. post. /requests. create item request");
        return itemRequestClient.create(requestDto, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAll(@RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. get. /requests. find all item requests");
        return itemRequestClient.findAllByRequester(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAllFromOthers(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                    @RequestParam(defaultValue = "0") @Valid @PositiveOrZero int from,
                                                    @RequestParam(defaultValue = "10") @Valid @Positive int size) {
        log.info("controller. get. /requests. find all item requests from others users");
        return itemRequestClient.findAllFromOthers(userId, from, size);
    }

    @GetMapping("/{itemRequestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findById(@PathVariable int itemRequestId,
                                           @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. get. /requests/{}. find item requests by id", itemRequestId);
        return itemRequestClient.findById(itemRequestId, userId);
    }
}
