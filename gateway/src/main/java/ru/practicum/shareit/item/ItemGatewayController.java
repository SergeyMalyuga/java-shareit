package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Map;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemGatewayController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> postItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                           @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemClient.postItem(userId, itemRequestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@PathVariable(name = "id") int itemId,
                                              @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemForOwner(@RequestHeader("X-Sharer-User-Id") int userId,
                                                     @PositiveOrZero @RequestParam(name = "from",
                                                             defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size",
                                                             defaultValue = "10") Integer size) {
        return itemClient.getAllItemForOwner(userId, from, size);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @PathVariable(name = "id") int itemId,
                                             @RequestBody Map<Object, Object> fields) {
        return itemClient.updateItem(userId, itemId, fields);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam(name = "text") String request,
                                             @RequestHeader("X-Sharer-User-Id") int userId,
                                             @PositiveOrZero @RequestParam(name = "from",
                                                     defaultValue = "0") Integer from,
                                             @Positive @RequestParam(name = "size",
                                                     defaultValue = "10") Integer size) {
        return itemClient.searchItem(request, userId, from, size);
    }

    public ResponseEntity<Object> addComment(@PathVariable int itemId, @RequestHeader("X-Sharer-User-Id")
    int userId, @RequestBody CommentRequestDto commentRequestDto) {
        return itemClient.addComment(itemId, userId, commentRequestDto);
    }
}