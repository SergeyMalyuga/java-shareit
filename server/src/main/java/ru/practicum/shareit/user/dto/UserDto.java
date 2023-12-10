package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

@Data
@Accessors(chain = true)
@EqualsAndHashCode
@ToString
@Component
public class UserDto {

    private int id;
    private String name;
    private String email;
}
