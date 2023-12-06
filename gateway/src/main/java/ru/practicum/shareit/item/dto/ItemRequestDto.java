package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private int id;
    @NotBlank(message = "Поле \'name\' не может быть пустым.")
    @NotNull(message = "Поле \'name\' не может быть пустым.")
    private String name;
    @NotBlank(message = "Поле \'description\' не может быть пустым.")
    private String description;
    @NotNull(message = "Поле \'available\' не может быть пустым.")
    private Boolean available;
    private int ownerId;
    private Integer requestId;
    private BookItemRequestDto lastBooking;
    private BookItemRequestDto nextBooking;
    private List<CommentRequestDto> comments = new ArrayList<>();
}
