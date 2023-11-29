package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemService itemService;
    private Item item;
    private Item item2;
    private Comment comment;
    private CommentDto commentDto;
    private ItemMapper itemMapper;
    private List<Item> itemList = new ArrayList<>();

    @BeforeEach
    private void setUp() {
        item = new Item().setId(1).setAvailable(true).setDescription("Аккумуляторная дрель")
                .setName("Дрель").setOwnerId(1);
        item2 = new Item().setId(2).setAvailable(true).setDescription("Ручной интсрумент").setName("Молоток")
                .setOwnerId(1).setRequestId(2);
        itemMapper = new ItemMapper();
        comment = new Comment().setId(1).setItem(item2)
                .setCreated(LocalDateTime.now()).setText("Отличный молоток");
        commentDto = new CommentDto().setId(1).setItem(itemMapper.itemDto(item2))
                .setCreated(LocalDateTime.now()).setText("Отличный молоток");
    }

    @Test
    void addItem() {
        Mockito.when(itemService.addItem(Mockito.anyInt(), Mockito.any(Item.class)))
                .thenReturn(itemMapper.itemDto(item));
        ItemDto saveItem = itemService.addItem(1, item);
        assertEquals(item.getId(), saveItem.getId());
    }

    @Test
    void updateItem() {
        item.setName("Аккумуляторная дрель");
        Mockito.when(itemService.updateItem(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyMap()))
                .thenReturn(itemMapper.itemDto(item));
        ItemDto updateItem = itemService.updateItem(1, 1, new HashMap<>());
        assertEquals("Аккумуляторная дрель", updateItem.getName());
    }

    @Test
    void getItemByIdDto() {
        Mockito.when(itemService.getItemByIdDto(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(itemMapper.itemDto(item2));
        ItemDto newItem = itemService.getItemByIdDto(1, 1);
        assertEquals(item2.getId(), newItem.getId());
    }

    @Test
    void getItemById() {
        Mockito.when(itemService.getItemById(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(item2);
        Item newItem = itemService.getItemById(1, 1);
        assertEquals(item2.getId(), newItem.getId());
    }

    @Test
    void getAllItemForOwner() {
        Mockito.when(itemService.getAllItemForOwner(Mockito.anyInt(), Mockito.any(Optional.class),
                Mockito.any(Optional.class))).thenReturn(itemList);
        List<ItemDto> newItemList = itemService.getAllItemForOwner(1, Optional.of(1),
                Optional.of(1));
        assertEquals(itemList, newItemList);
    }

    @Test
    void searchItem() {
        Mockito.when(itemService.searchItem(Mockito.anyString(), Mockito.any(Optional.class),
                Mockito.any(Optional.class))).thenReturn(itemList);
        List<ItemDto> newItemList = itemService.searchItem("Молоток", Optional.of(1),
                Optional.of(1));
        assertEquals(itemList, newItemList);
    }

    @Test
    void addComment() {
        Mockito.when(itemService.addComment(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(Comment.class)))
                .thenReturn(commentDto);
        CommentDto newComment = itemService.addComment(1, 1, comment);
        assertEquals(commentDto, newComment);
    }
}