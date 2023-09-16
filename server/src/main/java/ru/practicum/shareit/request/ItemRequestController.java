package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<ItemRequestDto> create(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                                 @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. post. /requests. create item request");
        return new ResponseEntity<>(itemRequestService.create(itemRequestDto, userId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> findAll(@RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. get. /requests. find all item requests");
        return new ResponseEntity<>(itemRequestService.findAllByRequester(userId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDto>> findAllFromOthers(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                                  @RequestParam(defaultValue = "0") @Valid @PositiveOrZero int from,
                                                                  @RequestParam(defaultValue = "10") @Valid @Positive int size) {
        log.info("controller. get. /requests. find all item requests from others users");
        return new ResponseEntity<>(itemRequestService.findAllFromOthers(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{itemRequestId}")
    public ResponseEntity<ItemRequestDto> findById(@PathVariable int itemRequestId,
                                                   @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. get. /requests/{}. find item requests by id", itemRequestId);
        return new ResponseEntity<>(itemRequestService.findById(itemRequestId, userId), HttpStatus.OK);
    }
}
