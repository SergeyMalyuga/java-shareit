package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.practicum.shareit.exception.EmailDuplicateException;
import ru.practicum.shareit.exception.NoDataFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int userId) {
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new NoDataFoundException("Пользователь с id:" + userId + " не найден.");
        }
    }

    @Override
    public void removeUserById(int userId) {
        getUserById(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public User updateUser(int userId, Map<Object, Object> fields) {
        if (getUserById(userId) != null) {
            User user = getUserById(userId);
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(User.class, (String) key);
                if (((String) key).equalsIgnoreCase("email") && !user.getEmail()
                        .equalsIgnoreCase((String) value)) {
                    checkEmailForDuplicate((String) value);
                }
                field.setAccessible(true);
                ReflectionUtils.setField(field, user, value);
            });
            return userRepository.save(user);
        } else {
            throw new NoDataFoundException("Пользователь с id:" + userId + " не найден.");
        }
    }

    private void checkEmailForDuplicate(String email) {
        List<User> userList = getAllUsers();
        for (User user : userList) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                throw new EmailDuplicateException("Email: " + email + " уже существует");
            }
        }
    }
}

