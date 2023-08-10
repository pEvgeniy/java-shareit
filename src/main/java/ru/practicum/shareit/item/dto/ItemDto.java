package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {

    public interface Create {
    }

    public interface Update {
    }

    @PositiveOrZero(groups = Update.class)
    Integer id;

    @NotBlank(groups = {Create.class, Update.class})
    String name;

    @NotBlank(groups = {Create.class, Update.class})
    String description;

    @NotNull(groups = {Create.class, Update.class})
    Boolean available;

    @PositiveOrZero(groups = Update.class)
    Integer request;
}
