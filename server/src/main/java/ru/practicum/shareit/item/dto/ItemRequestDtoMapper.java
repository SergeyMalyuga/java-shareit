package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;

import ru.practicum.shareit.item.model.Item;

@Component
public class ItemRequestDtoMapper {

    public ItemRequestDto toItemRequestDto(Item item) {
        return new ItemRequestDto().setId(item.getId())
                .setRequestId(item.getRequestId())
                .setName(item.getName())
                .setDescription(item.getDescription())
                .setAvailable(item.getAvailable());
    }
}
