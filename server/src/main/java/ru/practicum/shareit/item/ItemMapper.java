package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;


@Component
public class ItemMapper {

    public Item toItem(ItemDto item) {
        return new Item()
                .setName(item.getName())
                .setRequestId(item.getRequestId())
                .setOwnerId(item.getOwnerId())
                .setDescription(item.getDescription())
                .setAvailable(item.getAvailable());
    }
}
