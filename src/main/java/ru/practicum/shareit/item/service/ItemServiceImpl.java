package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.model.EntityNotFoundException;
import ru.practicum.shareit.exception.model.ItemUnavailableException;
import ru.practicum.shareit.exception.model.WrongParamException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentShortDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;

    private final CommentMapper commentMapper;

    private final BookingMapper bookingMapper;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. user with id = %s not found", userId)));
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(user);
        itemRepository.save(item);
        log.info("repository. item with id={} created", item.getId());
        return itemMapper.toItemDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> findAll(int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. user with id = %s not found", userId)));
        log.info("repository. item for user with id={} found", userId);
        return itemRepository.findByOwnerId(userId)
                .stream()
                .map(itemMapper::toItemDto)
                .map(this::setBookingsToItem)
                .map(this::setCommentsToItem)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto findById(int id, int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. user with id = %s not found", userId)));
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. item with id = %s not found", userId)));
        ItemDto itemDto = itemMapper.toItemDto(item);
        if (item.getOwner().getId() == userId) {
            setBookingsToItem(itemDto);
        }
        setCommentsToItem(itemDto);
        log.info("repository. item with id={} found", id);
        return itemDto;
    }

    @Override
    @Transactional
    public ItemDto update(int id, ItemDto itemDto, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. user with id = %s not found", userId)));
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. item with id = %s not found", id)));
        if (!item.getOwner().getId().equals(user.getId())) {
            throw new WrongParamException(String.format("repository. item with owner id = %s not found", userId));
        }
        updateItemFields(item, itemMapper.toItem(itemDto));
        log.info("repository. item with id={} updated", id);
        return itemMapper.toItemDto(item);
    }

    @Override
    @Transactional
    public int deleteById(int id) {
        itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. item with id = %s not found", id)));
        itemRepository.deleteById(id);
        log.info("repository. item with id={} deleted", id);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        log.info("repository. searching for items with text={}", text);
        return itemRepository.searchAvailableItemsByText(text.toLowerCase())
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto createComment(CommentShortDto commentShortDto, int itemId, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. user with id = %s not found", userId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("repository. item with id = %s not found", userId)));
        if (bookingRepository.findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(userId,
                itemId,
                BookingStatus.APPROVED, LocalDateTime.now()).isEmpty()) {
            throw new ItemUnavailableException(String.format("Item's booker id must equals to userId = %s", userId));
        }
        Comment comment = commentMapper.toComment(commentShortDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    private void updateItemFields(Item item, Item updates) {
        if (updates.getName() != null && !updates.getName().isBlank()) {
            item.setName(updates.getName());
        }
        if (updates.getDescription() != null && !updates.getDescription().isBlank()) {
            item.setDescription(updates.getDescription());
        }
        if (updates.getAvailable() != null) {
            item.setAvailable(updates.getAvailable());
        }
    }

    private ItemDto setBookingsToItem(ItemDto itemDto) {
        LocalDateTime dateTime = LocalDateTime.now();

        List<Booking> bookingsDesc =
                bookingRepository.findAllByItemIdAndStatusOrderByStartDesc(itemDto.getId(), BookingStatus.APPROVED);
        List<Booking> bookingsAsc =
                bookingRepository.findAllByItemIdAndStatusOrderByStartAsc(itemDto.getId(), BookingStatus.APPROVED);

        Booking lastBooking = bookingsDesc.stream()
                .filter(b -> b.getEnd().isBefore(dateTime) || (b.getStart().isBefore(dateTime) && b.getEnd().isAfter(dateTime)))
                .findFirst()
                .orElse(null);
        Booking nextBooking = bookingsAsc.stream()
                .filter(b -> b.getStart().isAfter(dateTime))
                .findFirst()
                .orElse(null);
        itemDto.setLastBooking(lastBooking == null ? null : bookingMapper.toBookingShortDto(lastBooking));
        itemDto.setNextBooking(bookingsAsc.size() <= 1 || nextBooking == null ? null : bookingMapper.toBookingShortDto(nextBooking));
        return itemDto;
    }

    private ItemDto setCommentsToItem(ItemDto itemDto) {
        itemDto.setComments(commentRepository.findAllByItem_Id(itemDto.getId())
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList()));
        return itemDto;
    }
}