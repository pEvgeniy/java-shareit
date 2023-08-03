package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * TODO Sprint add-controllers.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {

    @PositiveOrZero
    Integer id;

    @NotBlank
    String name;

    @NotBlank
    String description;

    @NotNull
    Boolean available;

    User owner;

    @PositiveOrZero
    Integer request;
}
