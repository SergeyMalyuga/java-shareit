package ru.practicum.shareit.user.dao;


import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserDao {

    UserDto addUser(User user);

    List<UserDto> getAllUsers();

    UserDto getUserDtoById(int userId);

    User getUserById(int userId);

    UserDto removeUserById(int userId);

    UserDto updateUser(int userId, Map<Object, Object> fields);
}
