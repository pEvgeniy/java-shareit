package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@Valid @RequestBody BookingShortDto bookingShortDto,
                             @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. post. /bookings. create booking request");
        return bookingService.create(bookingShortDto, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> findAllByBooker(@RequestParam(defaultValue = "ALL", required = false) String state,
                                            @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. get. /bookings. find all booking request");
        return bookingService.findAllByBooker(state, userId);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> findAllByOwner(@RequestParam(defaultValue = "ALL", required = false) String state,
                                           @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. get. /bookings. find all booking request");
        return bookingService.findAllByOwner(state, userId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto findById(@PathVariable int id,
                               @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. get. /bookings/{}. find booking by id request", id);
        return bookingService.findById(id, userId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto update(@RequestParam @NotNull Boolean approved,
                             @PathVariable int id,
                             @RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("controller. patch. /bookings/{}?approved={}. update booking request", id, approved);
        return bookingService.update(approved, id, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public int delete(@PathVariable int id) {
        log.info("controller. delete. /bookings/{}. delete booking by id request", id);
        return bookingService.deleteById(id);
    }

}
