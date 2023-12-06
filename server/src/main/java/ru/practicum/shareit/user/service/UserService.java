package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserDto addUser(UserDto user);

    List<UserDto> getAllUsersDto();

    List<User> getAllUsers();

    UserDto getUserDtoById(int userId);

    User getUserById(int userId);

    void removeUserById(int userId);

    UserDto updateUser(int userId, Map<Object, Object> fields);

    void checkEmailForDuplicate(String email);
}
