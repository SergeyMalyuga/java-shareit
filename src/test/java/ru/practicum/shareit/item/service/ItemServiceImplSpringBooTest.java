package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(properties = {"db.name=shareit_test"}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceImplSpringBooTest {

    private final EntityManager entityManager;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private Item item;
    private Item item2;
    private Item item3;
    private User user;
    private User user2;

    private Booking booking;
    private Comment comment;
    private CommentDto commentDto;
    private ItemMapper itemMapper;
    private List<Item> itemList = new ArrayList<>();

    @BeforeEach
    private void setUp() {
        user = new User().setEmail("serg@mail.ru").setName("Sergey");
        user2 = new User().setEmail("galina@mail.ru").setName("Galina");
        userService.addUser(user);
        userService.addUser(user2);
        item = new Item().setAvailable(true).setDescription("Аккумуляторная дрель")
                .setName("Дрель").setOwnerId(1);
        item2 = new Item().setAvailable(true).setDescription("Ручной интсрумент").setName("Молоток")
                .setOwnerId(1);
        item3 = new Item().setAvailable(true).setDescription("Пила").setName("Пила дисковая")
                .setOwnerId(1);
        itemService.addItem(1, item);
        itemService.addItem(1, item2);
        itemMapper = new ItemMapper();
        booking = new Booking().setItemId(1)
                .setStart(LocalDateTime.now().plusSeconds(1))
                .setEnd(LocalDateTime.now().plusSeconds(3));
        bookingService.addBooking(booking, 2);
        comment = new Comment().setText("Отличный молоток");
        commentDto = new CommentDto().setText("Отличный молоток");
    }

    @Test
    void addItem_Should_Return_Item() {
        itemService.addItem(1, item3);
        Item dbItem = entityManager.createQuery("FROM Item WHERE id = 1", Item.class).getSingleResult();
        assertThat(dbItem.getId(), equalTo(item.getId()));
        assertThat(dbItem.getName(), equalTo(item.getName()));
    }

    @Test
    void updateItem_Should_Return_UpdateItemName() {
        item2.setName("Кувалда");
        HashMap<Object, Object> fields = new HashMap<>();
        fields.put("name", "Кувалда");
        itemService.updateItem(1, 2, fields);
        Item dbItem = entityManager.createQuery("FROM Item WHERE id = 2", Item.class).getSingleResult();
        assertThat(dbItem.getName(), equalTo(item2.getName()));
    }

    @Test
    void getItemByIdDto_Should_Return_ItemDto_By_Id() {
        ItemDto itemDto = itemService.getItemByIdDto(1, 1);
        Item dbItem = entityManager.createQuery("FROM Item WHERE id = 1", Item.class).getSingleResult();
        assertThat(dbItem.getId(), equalTo(itemDto.getId()));
        assertThat(dbItem.getName(), equalTo(itemDto.getName()));
    }

    @Test
    void getItemById_Should_Return_Item_By_Id() {
        Item item = itemService.getItemById(1, 1);
        Item dbItem = entityManager.createQuery("FROM Item WHERE id = 1", Item.class).getSingleResult();
        assertThat(dbItem.getId(), equalTo(item.getId()));
        assertThat(dbItem.getName(), equalTo(item.getName()));
    }

    @Test
    void getAllItemForOwner_Should_Return_ItemDtoList() {
        List<ItemDto> itemDtoList = itemService.getAllItemForOwner(1, Optional.of(0),
                Optional.of(3));
        List<Item> dbItem = entityManager.createQuery("FROM Item WHERE ownerId = 1").getResultList();
        assertThat(dbItem.size(), equalTo(itemDtoList.size()));
    }

    @Test
    void searchItem_Should_Return_ItemDtoList() {
        List<ItemDto> itemDtoList = itemService.searchItem("Молоток", Optional.ofNullable(null),
                Optional.ofNullable(null));
        List<Item> dbItem = entityManager.createQuery("FROM Item WHERE name LIKE('%Молоток%')").getResultList();
        assertThat(dbItem.size(), equalTo(itemDtoList.size()));
    }

    @Test
    void addComment_Should_Return_Comment() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        CommentDto newComment = itemService.addComment(1, 2, comment);
        Comment dbComment = entityManager.createQuery("FROM Comment WHERE author = 2",
                Comment.class).getSingleResult();
        assertThat(newComment.getId(), equalTo(dbComment.getId()));
    }
}