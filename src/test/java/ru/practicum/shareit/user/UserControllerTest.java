package ru.practicum.shareit.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mvc;
    private UserMapper userMapper;
    private User user;
    private User user2;
    private User user3;
    private UserDto userDto;
    private UserDto userDto2;
    private UserDto userDto3;
    private List<User> listUser = new ArrayList<>();
    private List<UserDto> listUserDto = new ArrayList<>();
    private Gson gson;

    @BeforeEach
    void setUp() {
        gson = new Gson();
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
    void addUser() throws Exception {
        when(userService.addUser(Mockito.any(User.class))).thenReturn(userDto);
        mvc.perform(post("/users")
                        .content(gson.toJson(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId())))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsersDto()).thenReturn(listUserDto);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

        String json = gson.toJson(listUserDto);
        Configuration conf = Configuration.defaultConfiguration();
        String name0 = JsonPath.using(conf).parse(json).read("$[0]['name']");
        String name1 = JsonPath.using(conf).parse(json).read("$[1]['name']");
        String name2 = JsonPath.using(conf).parse(json).read("$[2]['name']");
        assertEquals("Sergey", name0);
        assertEquals("Galina", name1);
        assertEquals("Nick", name2);
    }

    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(Mockito.anyInt())).thenReturn(user2);

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user2.getId())))
                .andExpect(jsonPath("$.name", is(user2.getName())))
                .andExpect(jsonPath("$.email", is(user2.getEmail())));
    }

    @Test
    void removeUserById() throws Exception {
        mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

    }

    @Test
    void updateUser() throws Exception {
        userDto.setName("RedDragon");
        when(userService.updateUser(Mockito.anyInt(), Mockito.anyMap())).thenReturn(userDto);
        HashMap<Object, Object> query = new HashMap<>();
        query.put("name", "RedDragon");
        query.put("email", "update@user.com");

        mvc.perform(patch("/users/1")
                        .content(gson.toJson(query))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userDto.getName())));
    }
}