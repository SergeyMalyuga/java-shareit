package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;


@Component
public class UserMapper {
    public User toUser(UserDto user) {
        return new User()
                .setName(user.getName())
                .setEmail(user.getEmail());
    }
}
