package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRepositoryTest {

    private Item item;
    private Item item2;
    private Item item3;
    private User user;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ItemRepository itemRepository;


    @BeforeEach
    private void setUp() {
        item = new Item().setAvailable(true).setDescription("Аккумуляторная дрель")
                .setName("Дрель").setOwnerId(1);
        item2 = new Item().setAvailable(true).setDescription("Ручной интсрумент").setName("S Молоток")
                .setOwnerId(1);
        item3 = new Item().setAvailable(true).setDescription("Пила").setName("Пила дисковая")
                .setOwnerId(1);
        user = new User().setEmail("serg@mail.ru").setName("Sergey");
        entityManager.persist(user);
        entityManager.persist(item);
        entityManager.persist(item2);
        entityManager.persist(item3);

    }

    @Test
    void findByNameOrDescriptionWithPagination() {
        List<Item> itemList = itemRepository
                .findByNameOrDescriptionWithPagination("Молоток", PageRequest.of(0, 1)).getContent();
        assertEquals(1, itemList.size());
    }
}