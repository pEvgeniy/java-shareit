package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(groups = {Create.class, Update.class})
    private String name;

    @Email(groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class)
    private String email;
}
