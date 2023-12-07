package ru.practicum.shareit.request.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString

public class RequestDto {

    private int id;
    @NotBlank(message = "Поле не может быть пустым!")
    @NotNull(message = "Поле не может быть пустым!")
    private String description;
    private LocalDateTime created;
    private List<ItemRequestDto> items;
}
