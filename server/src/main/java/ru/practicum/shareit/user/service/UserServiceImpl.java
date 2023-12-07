package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.practicum.shareit.exception.EmailDuplicateException;
import ru.practicum.shareit.exception.NoDataFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDtoMapper;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDtoMapper userDtoMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        return userDtoMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getAllUsersDto() {
        return userRepository.findAll().stream().map(e -> userDtoMapper.toUserDto(e)).collect(Collectors.toList());
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDtoById(int userId) {
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isPresent()) {
            return userDtoMapper.toUserDto(optional.get());
        } else {
            throw new NoDataFoundException("Пользователь с id:" + userId + " не найден.");
        }
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
    public UserDto updateUser(int userId, Map<Object, Object> fields) {
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
        userRepository.save(user);
        return userDtoMapper.toUserDto(user);
    }

    @Override
    public void checkEmailForDuplicate(String email) {
        List<User> userList = getAllUsers();
        for (User user : userList) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                throw new EmailDuplicateException("Email: " + email + " уже существует");
            }
        }
    }
}

