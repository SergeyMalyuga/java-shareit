package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDtoMapper;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@Transactional
@SpringBootTest(properties = {"db.name=shareit_test"}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImplSpringBootTest {

    private final EntityManager entityManager;
    private final UserService userService;
    private User user;
    private User user2;
    private User user3;
    private UserDtoMapper userDtoMapper;
    private List<User> listUser = new ArrayList<>();

    @BeforeEach
    void setUp() {
        userDtoMapper = new UserDtoMapper();
        user = new User().setId(1).setEmail("serg@mail.ru").setName("Sergey");
        user2 = new User().setId(2).setEmail("galina@mail.ru").setName("Galina");
        user3 = new User().setId(3).setEmail("nick@mail.ru").setName("Nick");
        Collections.addAll(listUser, user, user2, user3);
        userService.addUser(userDtoMapper.toUserDto(user));
        userService.addUser(userDtoMapper.toUserDto(user2));
        userService.addUser(userDtoMapper.toUserDto(user3));
    }

    @Test
    void addUser_Should_Return_User() {
        User user4 = new User().setId(4).setEmail("reds@mail.ru").setName("Sergey");
        userService.addUser(userDtoMapper.toUserDto(user4));
        TypedQuery<User> query = entityManager.createQuery("From User WHERE id = :id", User.class);
        User dbUser = query.setParameter("id", user4.getId()).getSingleResult();
        assertThat(dbUser.getId(), equalTo(user4.getId()));
    }

    @Test
    void getAllUsersDto_Should_Return_UserDto_List() {
        List<UserDto> methodUserList = userService.getAllUsersDto();
        Query query = entityManager.createQuery("FROM User");
        List<User> userList = query.getResultList();
        assertThat(userList.size(), equalTo(methodUserList.size()));
    }

    @Test
    void getAllUsers_Should_Return_UserList() {
        List<User> methodUserList = userService.getAllUsers();
        Query query = entityManager.createQuery("FROM User");
        List<User> userList = query.getResultList();
        assertThat(userList.size(), equalTo(methodUserList.size()));
    }

    @Test
    void getUserDtoById_Should_Return_UserDto_By_Id() {
        UserDto userDto = userService.getUserDtoById(1);
        TypedQuery<User> query = entityManager.createQuery("FROM User WHERE id = 1", User.class);
        UserDto dbUserDto = userDtoMapper.toUserDto(query.getSingleResult());
        assertThat(dbUserDto.getId(), equalTo(userDto.getId()));
    }

    @Test
    void getUserById_Should_Return_User_By_Id() {
        User user = userService.getUserById(1);
        TypedQuery<User> query = entityManager.createQuery("FROM User WHERE id = 1", User.class);
        User dbUser = query.getSingleResult();
        assertThat(dbUser.getId(), equalTo(user.getId()));
    }

    @Test
    void removeUserById() {
        userService.removeUserById(1);
        Query query = entityManager.createQuery("FROM User");
        List<User> userList = query.getResultList();
        assertThat(userList, not(user));
    }

    @Test
    void updateUser_Should_Return_UpdateUser() {
        Map<Object, Object> fields = new HashMap<>();
        fields.put("name", "RedDragon");
        userService.updateUser(1, fields);
        TypedQuery<User> query = entityManager.createQuery("FROM User WHERE id = 1", User.class);
        User dbUser = query.getSingleResult();
        assertThat(dbUser.getName(), equalTo("RedDragon"));
    }
}