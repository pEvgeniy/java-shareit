package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.item.ItemController.X_SHARER_USER_ID;

@Slf4j
@Validated
@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@Valid @RequestBody BookingRequestDto bookingRequestDto,
                                         @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. post. /bookings. create booking request");
        return bookingClient.create(bookingRequestDto, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAllByBooker(@RequestParam(defaultValue = "ALL") String state,
                                                  @RequestHeader(X_SHARER_USER_ID) int userId,
                                                  @RequestParam(defaultValue = "0") @Valid @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Valid @Positive int size) {
        log.info("controller. get. /bookings. find all booking request");
        return bookingClient.findAllByBooker(state, userId, from, size);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAllByOwner(@RequestParam(defaultValue = "ALL") String state,
                                                 @RequestHeader(X_SHARER_USER_ID) int userId,
                                                 @RequestParam(defaultValue = "0") @Valid @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = "10") @Valid @Positive int size) {
        log.info("controller. get. /bookings. find all booking request");
        return bookingClient.findAllByOwner(state, userId, from, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findById(@PathVariable int id,
                                           @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. get. /bookings/{}. find booking by id request", id);
        return bookingClient.findById(id, userId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(@RequestParam @NotNull Boolean approved,
                                         @PathVariable int id,
                                         @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. patch. /bookings/{}?approved={}. update booking request", id, approved);
        return bookingClient.update(approved, id, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> delete(@PathVariable int id) {
        log.info("controller. delete. /bookings/{}. delete booking by id request", id);
        return bookingClient.deleteById(id);
    }

}
