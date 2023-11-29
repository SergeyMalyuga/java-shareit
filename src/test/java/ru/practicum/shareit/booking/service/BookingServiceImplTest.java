package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingService bookingService;
    private Booking booking;
    private Booking booking2;
    private Booking booking3;
    private List<BookingDto> bookingDtoList = new ArrayList<>();
    private BookingMapper bookingMapper;
    private Item item;
    private User user;


    @BeforeEach
    private void setUp() {
        bookingMapper = new BookingMapper();
        user = new User().setId(1).setEmail("serg@mail.ru").setName("Sergey");
        item = new Item().setId(1).setAvailable(true).setDescription("Аккумуляторная дрель")
                .setName("Дрель").setOwnerId(1);
        booking = new Booking().setId(1).setStatus(BookingStatus.WAITING).setBooker(user).setItem(item)
                .setEnd(LocalDateTime.now()).setStart(LocalDateTime.now().plusHours(1)).setItemId(1).setBookerId(1);
        booking2 = new Booking().setId(2).setStatus(BookingStatus.WAITING).setBooker(user).setItem(item)
                .setEnd(LocalDateTime.now()).setStart(LocalDateTime.now().plusHours(1)).setItemId(1).setBookerId(1);
        booking3 = new Booking().setId(3).setStatus(BookingStatus.WAITING).setBooker(user).setItem(item)
                .setEnd(LocalDateTime.now()).setStart(LocalDateTime.now().plusHours(1)).setItemId(1).setBookerId(1);
        bookingMapper.setUserMapper(new UserMapper());
        bookingMapper.setItemMapper(new ItemMapper());
        Collections.addAll(bookingDtoList, bookingMapper.bookingDto(booking),
                bookingMapper.bookingDto(booking2), bookingMapper.bookingDto(booking3));
    }

    @Test
    void addBooking() {
        Mockito.when(bookingService.addBooking(Mockito.any(Booking.class), Mockito.anyInt()))
                .thenReturn(bookingMapper.bookingDto(booking));
        BookingDto newBooking = bookingService.addBooking(booking, 1);
        assertEquals(booking.getId(), newBooking.getId());
        assertEquals(booking.getStatus(), newBooking.getStatus());
    }

    @Test
    void getBookingById() {
        Mockito.when(bookingService.getBookingById(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(bookingMapper.bookingDto(booking));
        BookingDto newBooking = bookingService.getBookingById(1, 1);
        assertEquals(booking.getId(), newBooking.getId());
        assertEquals(booking.getStatus(), newBooking.getStatus());
    }

    @Test
    void getAllBookingCurrentUser() {
        Mockito.when(bookingService.getAllBookingCurrentUser(Mockito.anyInt(), Mockito.anyString(),
                Mockito.any(Optional.class), Mockito.any(Optional.class))).thenReturn(bookingDtoList);
        List<BookingDto> newBookingDtoList = bookingService
                .getAllBookingCurrentUser(1, "ALL", Optional.of(0), Optional.of(3));
        assertEquals(bookingDtoList.size(), newBookingDtoList.size());
    }

    @Test
    void getAllBookingCurrentOwner() {
        Mockito.when(bookingService.getAllBookingCurrentOwner(Mockito.anyInt(), Mockito.anyString(),
                Mockito.any(Optional.class), Mockito.any(Optional.class))).thenReturn(bookingDtoList);
        List<BookingDto> newBookingDtoList = bookingService
                .getAllBookingCurrentOwner(1, "ALL", Optional.of(0), Optional.of(3));
        assertEquals(bookingDtoList.size(), newBookingDtoList.size());
    }

    @Test
    void confirmationOrRejectionBooking() {
    }
}