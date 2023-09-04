package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingSearchState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.model.AccessToEntityDeniedException;
import ru.practicum.shareit.exception.model.EntityNotFoundException;
import ru.practicum.shareit.exception.model.ItemUnavailableException;
import ru.practicum.shareit.exception.model.WrongParamException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDto create(BookingShortDto bookingShortDto, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. user with id = %s not found", userId)));
        Item item = itemRepository.findById(bookingShortDto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. item with id = %s not found", bookingShortDto.getItemId())));
        if (userId == item.getOwner().getId()) {
            throw new AccessToEntityDeniedException(String.format("repository. user with id = %s can not book his own item", userId));
        }
        if (!item.getAvailable()) {
            throw new ItemUnavailableException(String.format("repository. item with id = %s not available", item.getId()));
        }
        Booking booking = bookingMapper.toBooking(bookingShortDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking);
        log.info("repository. booking with id={} created", booking.getId());
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> findAllByBooker(String state, int userId, int from, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. user with id = %s not found", userId)));
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Booking> bookings = bookingRepository.findBookingByBookerIdOrderByStartDesc(userId, pageRequest);
        log.info("repository. booking for user with id={} found", userId);
        return findBookingAccordingToState(bookings, state)
                .stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> findAllByOwner(String state, int ownerId, int from, int size) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. user with id = %s not found", ownerId)));
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Booking> bookings = bookingRepository.findBookingByItemOwnerIdOrderByStartDesc(ownerId, pageRequest);
        log.info("repository. booking for user with id={} found", ownerId);
        return findBookingAccordingToState(bookings, state)
                .stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto findById(int id, int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. user with id = %s not found", userId)));
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. booking with id = %s not found", id)));
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new AccessToEntityDeniedException(String.format("repository. userId = %s should be item owner or booker", userId));
        }
        log.info("repository. booking with id={} found", id);
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto update(boolean isApproved, int id, int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. user with id = %s not found", userId)));
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. booking with id = %s not found", id)));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new AccessToEntityDeniedException(String.format("repository. userId = %s should be item owner", userId));
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ItemUnavailableException(String.format("repository. booking with id = %s is already approved", booking.getId()));
        }
        booking.setStatus(isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        log.info("repository. booking status with id={} updated to {}", id, booking.getStatus());
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public int deleteById(int id) {
        bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. booking with id = %s not found", id)));
        bookingRepository.deleteById(id);
        return id;
    }

    private List<Booking> findBookingAccordingToState(List<Booking> bookings, String state) {
        BookingSearchState castedState;
        try {
            castedState = BookingSearchState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
        switch (castedState) {
            case ALL:
                return bookings;
            case CURRENT:
                return bookings.stream()
                        .filter(b -> b.getStart().isBefore(LocalDateTime.now())
                                && b.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case WAITING:
                return bookings.stream()
                        .filter(b -> b.getStatus().equals(BookingStatus.WAITING))
                        .collect(Collectors.toList());
            case PAST:
                return bookings.stream()
                        .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case FUTURE:
                return bookings.stream()
                        .filter(b -> b.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case REJECTED:
                return bookings.stream()
                        .filter(b -> b.getStatus().equals(BookingStatus.REJECTED))
                        .collect(Collectors.toList());
            default:
                throw new WrongParamException(String.format("Expected state to be one of next states: %s", Arrays.toString(BookingStatus.values())));
        }
    }
}
