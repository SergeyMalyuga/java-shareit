package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    public User addUser(User user);

    public List<User> getAllUsers();

    public User getUserById(int userId);

    public void removeUserById(int userId);

    public User updateUser(int userId, Map<Object, Object> fields);
}
