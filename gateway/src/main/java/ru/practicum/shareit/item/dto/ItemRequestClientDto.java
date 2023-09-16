package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestClientDto {

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
}
