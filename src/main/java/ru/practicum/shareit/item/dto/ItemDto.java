package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode
@ToString
public class ItemDto {

    private int id;
    @NotBlank(message = "Поле \'name\' не может быть пустым.")
    @NotNull(message = "Поле \'name\' не может быть пустым.")
    private String name;
    @NotBlank(message = "Поле \'description\' не может быть пустым.")
    private String description;
    @NotNull(message = "Поле \'available\' не может быть пустым.")
    private Boolean available;
    private int ownerId;
    private String request;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<Comment> comments = new ArrayList<>();
}
