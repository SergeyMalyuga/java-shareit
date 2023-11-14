package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable(name = "id") int userId) {
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{id}")
    public void removeUserById(@PathVariable(name = "id") int userId) {
        userService.removeUserById(userId);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody Map<Object, Object> fields) {
        return userService.updateUser(id, fields);
    }
}
