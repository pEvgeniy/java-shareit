package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. post. /requests. create item request");
        return itemRequestService.create(itemRequestDto, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> findAll(@RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. get. /requests. find all item requests");
        return itemRequestService.findAllByRequester(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> findAllFromOthers(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                  @RequestParam(defaultValue = "0") @Valid @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Valid @Positive int size) {
        log.info("controller. get. /requests. find all item requests from others users");
        return itemRequestService.findAllFromOthers(userId, from, size);
    }

    @GetMapping("/{itemRequestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto findById(@PathVariable int itemRequestId,
                                   @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. get. /requests/{}. find item requests by id", itemRequestId);
        return itemRequestService.findById(itemRequestId, userId);
    }
}
