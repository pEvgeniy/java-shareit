package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    @PositiveOrZero(groups = Update.class)
    private Integer id;

    @NotBlank(groups = {Create.class, Update.class})
    private String name;

    @NotBlank(groups = {Create.class, Update.class})
    private String description;

    @NotNull(groups = {Create.class, Update.class})
    private Boolean available;

    @PositiveOrZero(groups = Update.class)
    private Integer requestId;

    private BookingShortDto lastBooking;

    private BookingShortDto nextBooking;

    private List<CommentDto> comments;
}
