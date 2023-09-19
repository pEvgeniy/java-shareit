package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.shareit.item.ItemController.X_SHARER_USER_ID;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequestDto> create(@RequestBody ItemRequestDto itemRequestDto,
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
                                                                  @RequestParam(defaultValue = "0") int from,
                                                                  @RequestParam(defaultValue = "10") int size) {
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
