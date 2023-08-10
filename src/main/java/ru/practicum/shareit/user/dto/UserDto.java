package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    public interface Create {
    }

    public interface Update {
    }

    @PositiveOrZero(groups = Update.class)
    Integer id;

    @NotBlank(groups = {Create.class, Update.class})
    String name;

    @Email(groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class)
    String email;
}
