package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDtoMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(properties = {"db.name=shareit_test"}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceImpSpringBootlTest {

    private final EntityManager entityManager;
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private final UserDtoMapper userMapper;
    private final ItemDtoMapper itemDtoMapper;

    private Booking booking;
    private Booking booking2;
    private Booking booking3;
    private List<BookingDto> bookingDtoList = new ArrayList<>();
    private BookingMapper bookingMapper;
    private Item item;
    private User user;
    private User user2;


    @BeforeEach
    private void setUp() {
        bookingMapper = new BookingMapper();
        user = new User().setEmail("serg@mail.ru").setName("Sergey");
        user2 = new User().setEmail("galina@mail.ru").setName("Galina");
        item = new Item().setId(1).setAvailable(true).setDescription("Аккумуляторная дрель")
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
        bookingMapper.setUserDtoMapper(new UserDtoMapper());
        bookingMapper.setItemDtoMapper(new ItemDtoMapper());
        Collections.addAll(bookingDtoList, bookingMapper.bookingDto(booking),
                bookingMapper.bookingDto(booking2), bookingMapper.bookingDto(booking3));
        userService.addUser(userMapper.toUserDto(user));
        userService.addUser(userMapper.toUserDto(user2));
        itemService.addItem(1, itemDtoMapper.itemDto(item));
    }

    @Test
    void addBooking_Should_Return_Booking() {
        bookingService.addBooking(booking, 2);
        Booking dbBooking = entityManager.createQuery("FROM Booking WHERE id = 1", Booking.class).getSingleResult();
        assertThat(dbBooking.getId(), equalTo(booking.getId()));
    }

    @Test
    void getBookingById_Should_Return_Booking_By_Id() {
        BookingDto newBookingDto = bookingService.addBooking(booking, 2);
        Booking dbBooking = entityManager.createQuery("FROM Booking WHERE id = 1", Booking.class).getSingleResult();
        assertThat(dbBooking.getId(), equalTo(newBookingDto.getId()));
    }

    @Test
    void getAllBookingCurrentUser_Should_Return_BookingDtoList_CurrentUser() {
        List<BookingDto> newBookingDtoList = bookingService.getAllBookingCurrentUser(1, "ALL",
                Optional.ofNullable(null), Optional.ofNullable(null));
        List<BookingDto> dbBookingDtoList = entityManager.createQuery("FROM Booking WHERE booker = 1")
                .getResultList();
        assertThat(dbBookingDtoList.size(), equalTo(newBookingDtoList.size()));
    }

    @Test
    void getAllBookingCurrentOwner_Should_Return_BookingDtoList_Owner() {
        List<BookingDto> newBookingDtoList = bookingService.getAllBookingCurrentOwner(1, "ALL",
                Optional.ofNullable(null), Optional.ofNullable(null));
        List<BookingDto> dbBookingDtoList = entityManager.createQuery("FROM Booking WHERE item.ownerId = 1")
                .getResultList();
        assertThat(dbBookingDtoList.size(), equalTo(newBookingDtoList.size()));
    }

    @Test
    void confirmationOrRejectionBooking() {
        bookingService.addBooking(booking, 2);
        bookingService.confirmationOrRejectionBooking(1, 1, "true");
        Booking dbBooking = entityManager.createQuery("FROM Booking WHERE id =1", Booking.class).getSingleResult();
        assertThat(dbBooking.getStatus().toString(), equalTo("APPROVED"));
    }
}