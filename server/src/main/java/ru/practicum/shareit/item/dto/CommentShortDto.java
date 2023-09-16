package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentShortDto {

    @NotBlank
    private String text;
}
