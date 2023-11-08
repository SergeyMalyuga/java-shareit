package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {

    public UserDto addUser(User user);

    public List<UserDto> getAllUsers();

    public UserDto getUserDtoById(int userId);

    public UserDto removeUserById(int userId);

    public UserDto updateUser(int userId, Map<Object, Object> fields);
}
