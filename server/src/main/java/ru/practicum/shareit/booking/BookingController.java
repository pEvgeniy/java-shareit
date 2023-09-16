package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.item.ItemController.X_SHARER_USER_ID;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> create(@Valid @RequestBody BookingShortDto bookingShortDto,
                                             @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. post. /bookings. create booking request");
        return new ResponseEntity<>(bookingService.create(bookingShortDto, userId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> findAllByBooker(@RequestParam(defaultValue = "ALL") String state,
                                                            @RequestHeader(X_SHARER_USER_ID) int userId,
                                                            @RequestParam(defaultValue = "0") @Valid @PositiveOrZero int from,
                                                            @RequestParam(defaultValue = "10") @Valid @Positive int size) {
        log.info("controller. get. /bookings. find all booking request");
        return new ResponseEntity<>(bookingService.findAllByBooker(state, userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> findAllByOwner(@RequestParam(defaultValue = "ALL") String state,
                                                           @RequestHeader(X_SHARER_USER_ID) int userId,
                                                           @RequestParam(defaultValue = "0") @Valid @PositiveOrZero int from,
                                                           @RequestParam(defaultValue = "10") @Valid @Positive int size) {
        log.info("controller. get. /bookings. find all booking request");
        return new ResponseEntity<>(bookingService.findAllByOwner(state, userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> findById(@PathVariable int id,
                                               @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. get. /bookings/{}. find booking by id request", id);
        return new ResponseEntity<>(bookingService.findById(id, userId), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookingDto> update(@RequestParam @NotNull Boolean approved,
                                             @PathVariable int id,
                                             @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. patch. /bookings/{}?approved={}. update booking request", id, approved);
        return new ResponseEntity<>(bookingService.update(approved, id, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> delete(@PathVariable int id) {
        log.info("controller. delete. /bookings/{}. delete booking by id request", id);
        return new ResponseEntity<>(bookingService.deleteById(id), HttpStatus.OK);
    }
}
