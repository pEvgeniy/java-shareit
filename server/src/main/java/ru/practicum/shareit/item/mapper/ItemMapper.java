package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForRequests;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "requestId", source = "request.id")
    ItemDto toItemDto(Item item);

    Item toItem(ItemDto itemDto);

    @Mapping(target = "requestId", source = "request.id")
    ItemDtoForRequests toItemDtoForRequests(Item item);
}
