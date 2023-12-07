package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private int id;
    @NotBlank(message = "Поле не может быть пустым!")
    @NotNull(message = "Поле не может быть пустым!")
    private String description;
    private LocalDateTime created;
    private List<ItemRequestDto> items;
}
