package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/items")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemController {

    ItemService itemService;

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.create(itemDto, userId);
    }

    @GetMapping
    public List<ItemDto> findAll(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/{id}")
    public ItemDto findById(@Valid @PathVariable int id) {
        return itemService.findById(id);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable int id,
                          @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.update(id, itemDto, userId);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@Valid @PathVariable int id) {
        return itemService.deleteById(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }
}
