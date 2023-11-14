package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemService {
    Item addItem(int userId, Item item);

    Item updateItem(int userId, int itemId, Map<Object, Object> fields);

    ItemDto getItemByIdDto(int itemId, int userId);

    Item getItemById(int itemId, int userId);

    List<ItemDto> getAllItemForOwner(int ownerId);

    List<Item> searchItem(String request);

    Comment addComment(int itemId, int userId, Comment comment);
}
