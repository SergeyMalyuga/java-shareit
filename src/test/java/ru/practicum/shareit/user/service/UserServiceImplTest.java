package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.EmailDuplicateException;
import ru.practicum.shareit.exception.NoDataFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserService userService = new UserServiceImpl();
    @Mock
    private UserRepository userRepository;
    @Mock(answer = Answers.CALLS_REAL_METHODS)
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
    void addUser_Should_Return_User() {
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        UserDto saveUser = userService.addUser(user);
        assertNotNull(saveUser);
        assertEquals(user.getId(), saveUser.getId());
    }

    @Test
    void getAllUsersDto_Should_Return_All_UsersDto() {
        Mockito.when(userRepository.findAll()).thenReturn(listUser);
        List<UserDto> newList = userService.getAllUsersDto();
        assertEquals(3, newList.size());
    }

    @Test
    void getAllUsers_Should_Return_All_Users() {
        Mockito.when(userRepository.findAll()).thenReturn(listUser);
        List<User> newList = userService.getAllUsers();
        assertEquals(3, newList.size());
    }

    @Test
    void getUserDtoById_Should_Return_UserDto_ById() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        UserDto newUser = userService.getUserDtoById(2);
        assertEquals(1, newUser.getId());
    }

    @Test
    void getUserDtoById_Should_Return_UserDto_ById_Should_Throw_NoDataFoundException() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(null));
        assertThrows(NoDataFoundException.class, () -> userService.getUserDtoById(1));
    }

    @Test
    void getUserById_Should_Return_User_ById() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user3));
        UserDto newUser = userService.getUserDtoById(2);
        assertEquals(3, newUser.getId());
    }

    @Test
    void getUserDtoById_Should_Return_User_ById_Should_Throw_NoDataFoundException() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(null));
        assertThrows(NoDataFoundException.class, () -> userService.getUserById(1));
    }

    @Test
    void removeUserById_Should_Voice_Method_UserRepository_DeleteById() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        userService.removeUserById(1);
        Mockito.verify(userRepository, Mockito.timeout(1)).deleteById(1);
    }

    @Test
    void updateUser_Should_Update_User_Name() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Map<Object, Object> fields = new HashMap<>();
        fields.put("name", "Napoleon");
        UserDto userAfterUpdate = userService.updateUser(1, fields);
        assertEquals("Napoleon", userAfterUpdate.getName());
    }

    @Test
    void updateUser_Should_Throw_EmailDuplicateException() {
        Map<Object, Object> fields = new HashMap<>();
        fields.put("name", "Napoleon");
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenAnswer(invocationOnMock -> {
            int userId = invocationOnMock.getArgument(0, Integer.class);
            if (userId == 0) {
                throw new EmailDuplicateException("");
            }
            throw new EmailDuplicateException("");
        });
        assertThrows(EmailDuplicateException.class, () -> userService.updateUser(1, fields));
    }

    @Test
    void updateUser_Should_Throw_NoDataFoundException() {
        Map<Object, Object> fields = new HashMap<>();
        fields.put("name", "Napoleon");
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(null));
        assertThrows(NoDataFoundException.class, () -> userService.updateUser(1, fields));
    }

    @Test
    void addUser_Should_Throw_EmailDuplicateException() {
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocationOnMock -> {
            User userId = invocationOnMock.getArgument(0, User.class);
            if (userId != null) {
                throw new EmailDuplicateException("");
            }
            throw new EmailDuplicateException("");
        });
        assertThrows(EmailDuplicateException.class, () -> userService.addUser(user));
    }

    @Test
    void checkEmailForDuplicate_Falls() {
        Mockito.when(userRepository.findAll()).thenReturn(listUser);
        assertThrows(EmailDuplicateException.class, () -> userService.checkEmailForDuplicate("serg@mail.ru"));
    }

    @Test
    void checkEmailForDuplicate_Accept() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user3));
        assertDoesNotThrow(() -> userService.checkEmailForDuplicate("serg@mail.ru"));
    }
}