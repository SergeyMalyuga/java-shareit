package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserDto addUser(User user);

    List<UserDto> getAllUsersDto();

    List<User> getAllUsers();

    UserDto getUserDtoById(int userId);

    User getUserById(int userId);

    void removeUserById(int userId);

    UserDto updateUser(int userId, Map<Object, Object> fields);

    void checkEmailForDuplicate(String email);
}
