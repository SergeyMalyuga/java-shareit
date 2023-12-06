package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ItemService {
    ItemDto addItem(int userId, ItemDto itemDto);

    ItemDto updateItem(int userId, int itemId, Map<Object, Object> fields);

    ItemDto getItemByIdDto(int itemId, int userId);

    Item getItemById(int itemId, int userId);

    List<ItemDto> getAllItemForOwner(int ownerId, Optional<Integer> from, Optional<Integer> size);

    List<ItemDto> searchItem(String request, Optional<Integer> from, Optional<Integer> size);

    CommentDto addComment(int itemId, int userId, Comment comment);
}
