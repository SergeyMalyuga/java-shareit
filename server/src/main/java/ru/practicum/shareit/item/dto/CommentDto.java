package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Component
@Getter
@Setter
@Accessors(chain = true)
public class CommentDto {

    private int id;

    private String text;

    private ItemDto item;

    private UserDto author;

    private LocalDateTime created;

    String authorName;
}
