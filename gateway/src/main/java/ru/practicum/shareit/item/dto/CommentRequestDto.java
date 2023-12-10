package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserRequestDto;

import java.time.LocalDateTime;

@Component
@Getter
@Setter
@Accessors(chain = true)
public class CommentRequestDto {

    private int id;

    private String text;

    private ItemRequestDto item;

    private UserRequestDto author;

    private LocalDateTime created;

    String authorName;
}
