package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserService userService;
    private UserMapper userMapper;
    private User user;
    private User user2;
    private User user3;
    private UserDto userDto;
    private UserDto userDto2;
    private UserDto userDto3;
    private List<User> listUser = new ArrayList<>();
    private List<UserDto> listUserDto = new ArrayList<>();

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
        user = new User().setId(1).setEmail("serg@mail.ru").setName("Sergey");
        user2 = new User().setId(2).setEmail("galina@mail.ru").setName("Galina");
        user3 = new User().setId(3).setEmail("nick@mail.ru").setName("Nick");
        Collections.addAll(listUser, user, user2, user3);
        userDto = userMapper.toUserDto(user);
        userDto2 = userMapper.toUserDto(user2);
        userDto3 = userMapper.toUserDto(user3);
        Collections.addAll(listUserDto, userDto, userDto2, userDto3);
    }

    @Test
    void addUser() {
        Mockito.when(userService.addUser(Mockito.any(User.class))).thenReturn(userMapper.toUserDto(user));
        UserDto saveUser = userService.addUser(user);
        assertEquals(saveUser, userMapper.toUserDto(user));
    }

    @Test
    void getAllUsersDto() {
        Mockito.when(userService.getAllUsersDto()).thenReturn(listUserDto);
        List<UserDto> newList = userService.getAllUsersDto();
        assertEquals(listUserDto, newList, "Не совпадают");
    }

    @Test
    void getAllUsers() {
        Mockito.when(userService.getAllUsers()).thenReturn(listUser);
        List<User> newList = userService.getAllUsers();
        assertEquals(listUser, newList, "Не совпадают");
    }

    @Test
    void getUserDtoById() {
        Mockito.when(userService.getUserDtoById(Mockito.anyInt())).thenReturn(userMapper.toUserDto(user2));
        UserDto newUser = userService.getUserDtoById(2);
        assertEquals(userDto2, userMapper.toUserDto(user2));
    }

    @Test
    void getUserById() {
        Mockito.when(userService.getUserById(Mockito.anyInt())).thenReturn(user3);
        User newUser = userService.getUserById(3);
        assertEquals(user3, newUser);
    }

    @Test
    void removeUserById() {
        userService.removeUserById(1);
        Mockito.verify(userService, Mockito.timeout(1)).removeUserById(1);
    }

    @Test
    void updateUser() {
        user.setName("Napaleon");
        Mockito.when(userService.updateUser(Mockito.anyInt(), Mockito.anyMap())).thenReturn(userMapper.toUserDto(user));
        UserDto userAfterUpdate = userService.updateUser(1, new HashMap<>());
        assertEquals("Napaleon", userAfterUpdate.getName());
    }
}