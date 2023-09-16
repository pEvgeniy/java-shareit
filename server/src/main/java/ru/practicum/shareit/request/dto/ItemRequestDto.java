package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDtoForRequests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    @PositiveOrZero
    private Integer id;

    @NotBlank
    private String description;

    @PastOrPresent
    private LocalDateTime created;

    private List<ItemDtoForRequests> items;

}
