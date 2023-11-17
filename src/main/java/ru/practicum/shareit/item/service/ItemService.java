package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemService {
    ItemDto addItem(int userId, Item item);

    ItemDto updateItem(int userId, int itemId, Map<Object, Object> fields);

    ItemDto getItemByIdDto(int itemId, int userId);

    Item getItemById(int itemId, int userId);

    List<ItemDto> getAllItemForOwner(int ownerId);

    List<ItemDto> searchItem(String request);

    CommentDto addComment(int itemId, int userId, Comment comment);
}
