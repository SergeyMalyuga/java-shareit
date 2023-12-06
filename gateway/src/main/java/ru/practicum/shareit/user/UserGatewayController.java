package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserGatewayController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> postUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        return userClient.postUser(userRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> postUser() {
        return userClient.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable(name = "id") int userId) {
        return userClient.getUserById(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeUserById(@PathVariable(name = "id") int userId) {
        return userClient.removeUserById(userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(name = "id") int userId,
                                             @RequestBody Map<Object, Object> body) {
        return userClient.updateUser(userId, body);
    }

}
