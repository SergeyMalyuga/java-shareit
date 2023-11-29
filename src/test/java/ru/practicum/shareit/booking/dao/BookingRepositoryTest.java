package ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private BookingRepository bookingRepository;

    private Booking booking;
    private Booking booking2;
    private Booking booking3;
    private BookingMapper bookingMapper;
    private Item item;
    private User user;
    private User user2;


    @BeforeEach
    private void setUp() {
        bookingMapper = new BookingMapper();
        user = new User().setEmail("serg@mail.ru").setName("Sergey");
        user2 = new User().setEmail("galina@mail.ru").setName("Galina");
        item = new Item().setAvailable(true).setDescription("Аккумуляторная дрель")
                .setName("Дрель").setOwnerId(1);
        booking = new Booking().setStatus(BookingStatus.WAITING).setBooker(user).setItem(item)
                .setStart(LocalDateTime.now().plusMinutes(1)).setEnd(LocalDateTime.now()
                        .plusHours(1)).setItemId(1).setBookerId(1);
        booking2 = new Booking().setStatus(BookingStatus.WAITING).setBooker(user).setItem(item)
                .setStart(LocalDateTime.now().plusMinutes(1)).setEnd(LocalDateTime.now()
                        .plusHours(1)).setItemId(1).setBookerId(1);
        booking3 = new Booking().setStatus(BookingStatus.WAITING).setBooker(user).setItem(item)
                .setStart(LocalDateTime.now().plusMinutes(1)).setEnd(LocalDateTime.now()
                        .plusHours(1)).setItemId(1).setBookerId(1);
        bookingMapper.setUserMapper(new UserMapper());
        bookingMapper.setItemMapper(new ItemMapper());
        entityManager.persist(user);
        entityManager.persist(user2);
        entityManager.persist(item);
        entityManager.persist(booking);
        entityManager.persist(booking2);
        entityManager.persist(booking3);

    }

    @Test
    void findByBookerIdWithPagination() {
        List<Booking> dbBookingList = entityManager.createQuery("FROM Booking WHERE booker = 1", Booking.class)
                .getResultList();
        assertEquals(3, dbBookingList.size());
    }
}