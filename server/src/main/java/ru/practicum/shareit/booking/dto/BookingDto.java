package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
public class BookingDto {

    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private int itemId;
    private BookingStatus status;
    private ItemDto item;
    private UserDto booker;
    int bookerId;
}
