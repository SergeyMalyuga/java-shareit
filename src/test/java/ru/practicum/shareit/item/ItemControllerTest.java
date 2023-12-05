package ru.practicum.shareit.item;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import ru.practicum.shareit.LocalDateTimeAdapter;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private Item item;
    private Item item2;
    private User user;
    private Comment comment;
    private CommentDto commentDto;
    private ItemDtoMapper itemDtoMapper;
    private Gson gson;
    private List<Item> itemList = new ArrayList<>();

    @BeforeEach
    private void setUp() {
        gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        user = new User().setId(1).setEmail("serg@mail.ru").setName("Sergey");
        item = new Item().setId(1).setAvailable(true).setDescription("Аккумуляторная дрель")
                .setName("Дрель").setOwnerId(1);
        item2 = new Item().setId(2).setAvailable(true).setDescription("Ручной интсрумент").setName("Молоток")
                .setOwnerId(1).setRequestId(2);
        Collections.addAll(itemList, item, item2);
        itemDtoMapper = new ItemDtoMapper();
        comment = new Comment().setId(1).setItem(item2)
                .setCreated(LocalDateTime.now()).setText("Отличный молоток");
        commentDto = new CommentDto().setId(1).setItem(itemDtoMapper.itemDto(item2))
                .setCreated(LocalDateTime.now()).setText("Отличный молоток");
    }

    @Test
    void addItem_Should_Return_Item() throws Exception {
        when(itemService.addItem(Mockito.anyInt(), Mockito.any(ItemDto.class))).thenReturn(itemDtoMapper.itemDto(item));
        mvc.perform(post("/items")
                        .content(gson.toJson(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId())))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())));
    }

    @Test
    void getItemById_Should_Return_Item_By_Id() throws Exception {
        when(itemService.getItemByIdDto(Mockito.anyInt(), Mockito.anyInt())).thenReturn(itemDtoMapper.itemDto(item));
        mvc.perform(get("/items/1").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId())))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())));
    }

    @Test
    void getAllItemForOwner_Should_Return_ItemDtoList() throws Exception {
        when(itemService.getAllItemForOwner(Mockito.anyInt(), Mockito.any(Optional.class),
                Mockito.any(Optional.class))).thenReturn(itemList);

        mvc.perform(get("/items").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        String json = gson.toJson(itemList);
        Configuration conf = Configuration.defaultConfiguration();
        String name0 = JsonPath.using(conf).parse(json).read("$[0]['name']");
        String name1 = JsonPath.using(conf).parse(json).read("$[1]['name']");
        assertEquals("Дрель", name0);
        assertEquals("Молоток", name1);
    }

    @Test
    void updateItem_Should_Return_UpdateItemName() throws Exception {
        item2.setName("Дисковая пила");
        when(itemService.updateItem(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyMap()))
                .thenReturn(itemDtoMapper.itemDto(item2));
        Map<Object, Object> query = new HashMap<>();
        query.put("name", "Дисковая пила");

        mvc.perform(patch("/items/1").content(gson.toJson(query)).header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(item2.getName())));
    }

    @Test
    void searchItem_Should_Return_ItemDtoList() throws Exception {
        when(itemService.searchItem(Mockito.anyString(), Mockito.any(Optional.class),
                Mockito.any(Optional.class))).thenReturn(itemList);

        mvc.perform(get("/items/search").param("text", "Молоток")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void addComment_Should_Return_Comment() throws Exception {
        when(itemService.addComment(Mockito.anyInt(), Mockito.anyInt(),
                Mockito.any(Comment.class))).thenReturn(commentDto);

        Map<String, String> query = new HashMap<>();
        query.put("text", "Отличный молоток");

        mvc.perform(post("/items/1/comment").header("X-Sharer-User-Id", 1)
                        .content(gson.toJson(query))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("Отличный молоток")))
                .andExpect(jsonPath("$.id", is(commentDto.getId())));
    }
}