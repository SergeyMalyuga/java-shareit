package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NoDataFoundException;
import ru.practicum.shareit.exception.UnavailableItemException;
import ru.practicum.shareit.exception.UnavailableStateException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    private BookingService bookingService = new BookingServiceImpl();
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private BookingMapper bookingMapper;
    private Booking booking;
    private Booking booking2;
    private Booking booking3;
    private List<Booking> bookingList = new ArrayList<>();
    private List<Item> itemsList = new ArrayList<>();
    private Item item;
    private Item item2;
    private User user;
    private User user2;


    @BeforeEach
    private void setUp() {

        user = new User().setId(1).setEmail("serg@mail.ru").setName("Sergey");
        user2 = new User().setId(2).setEmail("Galina@mail.ru").setName("Galina");
        item = new Item().setId(1).setAvailable(true).setDescription("Аккумуляторная дрель")
                .setName("Дрель").setOwnerId(2);
        item2 = new Item().setId(1).setAvailable(false).setDescription("Аккумуляторная дрель")
                .setName("Дрель").setOwnerId(2);
        booking = new Booking().setId(1).setStatus(BookingStatus.WAITING).setStart(LocalDateTime.now().plusMinutes(1))
                .setEnd(LocalDateTime.now().plusHours(1)).setItemId(1).setBookerId(1).setBooker(user).setItem(item);

        booking2 = new Booking().setId(2).setStatus(BookingStatus.APPROVED).setBooker(user).setItem(item)
                .setEnd(LocalDateTime.now()).setStart(LocalDateTime.now().plusHours(1)).setItemId(1).setBookerId(1);
        booking3 = new Booking().setId(3).setStatus(BookingStatus.WAITING).setBooker(user).setItem(item)
                .setEnd(LocalDateTime.now()).setStart(LocalDateTime.now().plusHours(1)).setItemId(1).setBookerId(1);
        bookingMapper.setUserMapper(new UserMapper());
        bookingMapper.setItemMapper(new ItemMapper());
        Collections.addAll(itemsList, item);
        Collections.addAll(bookingList, booking, booking2, booking3);
    }

    @Test
    void addBooking_Should_Return_Booking() {
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        BookingDto bookingDto = bookingService.addBooking(booking, 1);
        assertEquals(bookingDto.getId(), 1);
        assertEquals(bookingDto.getBooker().getName(), "Sergey");
    }

    @Test
    void addBooking_Should_Throw_NoDataFoundException() {
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user2));
        assertThrows(NoDataFoundException.class, () -> bookingService.addBooking(booking, 2));
    }

    @Test
    void addBooking_Should_Throw_UnavailableItemException() {
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item2));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user2));
        assertThrows(UnavailableItemException.class, () -> bookingService.addBooking(booking, 1));
    }

    @Test
    void getBookingById_Should_Return_Booking() {
        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking2));
        BookingDto bookingDto = bookingService.getBookingById(2, 1);
        assertNotNull(bookingDto);
    }

    @Test
    void getBookingById_Should_Throw_NoDataFoundException() {
        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking2));
        assertThrows(NoDataFoundException.class, () -> bookingService.getBookingById(2, 3));

    }

    @Test
    void getAllBookingCurrentUser_Should_Return_BookingsList_Current_User() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.findByBookerEquals(Mockito.any(User.class)))
                .thenReturn(bookingList);
        List<BookingDto> bookingDtoList = bookingService.getAllBookingCurrentUser(1, "ALL",
                Optional.ofNullable(null), Optional.ofNullable(null));
        assertNotNull(bookingDtoList.size());
    }

    @Test
    void getAllBookingCurrentUser_Should_Return_BookingsList_Current_User_Pagination() {
        Page<Booking> page = PageableExecutionUtils.getPage(bookingList,
                PageRequest.of(0, 3), () -> bookingList.size());
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.findByBookerIdWithPagination(Mockito.anyInt(),
                Mockito.any(Pageable.class))).thenReturn(page);
        List<BookingDto> bookingDtoList = bookingService.getAllBookingCurrentUser(1, "ALL",
                Optional.ofNullable(0), Optional.ofNullable(3));
        assertNotNull(bookingDtoList.size());
    }

    @Test
    void getAllBookingCurrentUser_Should_Return_BookingsList_Current_User_Pagination_Throw_UnavailableItemException() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        assertThrows(UnavailableItemException.class, () -> bookingService.getAllBookingCurrentUser(1, "ALL",
                Optional.ofNullable(0), Optional.ofNullable(-3)));
    }

    @Test
    void getAllBookingCurrentOwnerShould_Return_BookingsList_Owner() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findByOwnerId(Mockito.anyInt())).thenReturn(itemsList);
        Mockito.when(bookingRepository.findByItemEquals(Mockito.any(Item.class)))
                .thenReturn(bookingList);
        List<BookingDto> bookingDtoList = bookingService.getAllBookingCurrentOwner(2, "ALL",
                Optional.ofNullable(null), Optional.ofNullable(null));
        assertNotNull(bookingDtoList.size());
    }

    @Test
    void getAllBookingCurrentOwnerShould_Return_BookingsList_Owner_Pagination() {
        Page<Booking> page = PageableExecutionUtils.getPage(bookingList,
                PageRequest.of(0, 3), () -> bookingList.size());
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findByOwnerId(Mockito.anyInt())).thenReturn(itemsList);
        Mockito.when(bookingRepository.findByItemOwnerId(Mockito.anyInt(),
                Mockito.any(Pageable.class))).thenReturn(page);
        List<BookingDto> bookingDtoList = bookingService.getAllBookingCurrentOwner(2, "ALL",
                Optional.ofNullable(0), Optional.ofNullable(3));
        assertNotNull(bookingDtoList.size());
    }

    @Test
    void confirmationOrRejectionBooking_Approved() {
        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user2));
        BookingDto bookingDto = bookingService.confirmationOrRejectionBooking(1, 2, "true");
        assertEquals(BookingStatus.APPROVED, bookingDto.getStatus());
    }

    @Test
    void confirmationOrRejectionBooking_Rejected() {
        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user2));
        BookingDto bookingDto = bookingService.confirmationOrRejectionBooking(1, 2, "false");
        assertEquals(BookingStatus.REJECTED, bookingDto.getStatus());
    }

    @Test
    void confirmationOrRejectionBooking_Throw_UnavailableItemException() {
        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking2));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user2));
        assertThrows(UnavailableItemException.class,
                () -> bookingService.confirmationOrRejectionBooking(1, 2, "true"));
    }

    @Test
    void confirmationOrRejectionBooking_Throw_Unknown_Status() {
        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking2));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user2));
        assertThrows(UnavailableItemException.class,
                () -> bookingService.confirmationOrRejectionBooking(1, 2, "tru"));
    }

    @Test
    void getBookingBeOwner_Should_Return_Booking() {
        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user2));
        Booking newBooking = bookingService.getBookingByOwner(1, 2);
        assertNotNull(newBooking);
    }

    @Test
    void getBookingBeOwner_Should_Throw_NoDataFoundException() {
        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        assertThrows(NoDataFoundException.class, () -> bookingService.getBookingByOwner(1, 1));
    }

    @Test
    void getAllBookingCurrentOwner_Rejected() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findByOwnerId(Mockito.anyInt())).thenReturn(itemsList);
        Mockito.when(bookingRepository.findByItemEquals(Mockito.any(Item.class)))
                .thenReturn(bookingList);
        List<BookingDto> bookingDtoList = bookingService.getAllBookingCurrentOwner(2, "REJECTED",
                Optional.ofNullable(null), Optional.ofNullable(null));
        assertNotNull(bookingDtoList.size());
    }

    @Test
    void getAllBookingCurrentOwner_Waiting() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findByOwnerId(Mockito.anyInt())).thenReturn(itemsList);
        Mockito.when(bookingRepository.findByItemEquals(Mockito.any(Item.class)))
                .thenReturn(bookingList);
        List<BookingDto> bookingDtoList = bookingService.getAllBookingCurrentOwner(2, "WAITING",
                Optional.ofNullable(null), Optional.ofNullable(null));
        assertNotNull(bookingDtoList.size());
    }

    @Test
    void getAllBookingCurrentOwner_Future() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findByOwnerId(Mockito.anyInt())).thenReturn(itemsList);
        Mockito.when(bookingRepository.findByItemEquals(Mockito.any(Item.class)))
                .thenReturn(bookingList);
        List<BookingDto> bookingDtoList = bookingService.getAllBookingCurrentOwner(2, "FUTURE",
                Optional.ofNullable(null), Optional.ofNullable(null));
        assertNotNull(bookingDtoList.size());
    }

    @Test
    void getAllBookingCurrentOwner_Past() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findByOwnerId(Mockito.anyInt())).thenReturn(itemsList);
        Mockito.when(bookingRepository.findByItemEquals(Mockito.any(Item.class)))
                .thenReturn(bookingList);
        List<BookingDto> bookingDtoList = bookingService.getAllBookingCurrentOwner(2, "PAST",
                Optional.ofNullable(null), Optional.ofNullable(null));
        assertNotNull(bookingDtoList.size());
    }

    @Test
    void getAllBookingCurrentOwner_Current() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findByOwnerId(Mockito.anyInt())).thenReturn(itemsList);
        Mockito.when(bookingRepository.findByItemEquals(Mockito.any(Item.class)))
                .thenReturn(bookingList);
        List<BookingDto> bookingDtoList = bookingService.getAllBookingCurrentOwner(2, "CURRENT",
                Optional.ofNullable(null), Optional.ofNullable(null));
        assertNotNull(bookingDtoList.size());
    }

    @Test
    void getAllBookingCurrentUser_Rejected() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.findByBookerEquals(Mockito.any(User.class)))
                .thenReturn(bookingList);
        List<BookingDto> bookingDtoList = bookingService.getAllBookingCurrentUser(1, "REJECTED",
                Optional.ofNullable(null), Optional.ofNullable(null));
        assertNotNull(bookingDtoList.size());
    }

    @Test
    void getAllBookingCurrentUser_WAITING() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.findByBookerEquals(Mockito.any(User.class)))
                .thenReturn(bookingList);
        List<BookingDto> bookingDtoList = bookingService.getAllBookingCurrentUser(1, "WAITING",
                Optional.ofNullable(null), Optional.ofNullable(null));
        assertNotNull(bookingDtoList.size());
    }

    @Test
    void getAllBookingCurrentUser_Future() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.findByBookerEquals(Mockito.any(User.class)))
                .thenReturn(bookingList);
        List<BookingDto> bookingDtoList = bookingService.getAllBookingCurrentUser(1, "FUTURE",
                Optional.ofNullable(null), Optional.ofNullable(null));
        assertNotNull(bookingDtoList.size());
    }

    @Test
    void getAllBookingCurrentUser_Past() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.findByBookerEquals(Mockito.any(User.class)))
                .thenReturn(bookingList);
        List<BookingDto> bookingDtoList = bookingService.getAllBookingCurrentUser(1, "PAST",
                Optional.ofNullable(null), Optional.ofNullable(null));
        assertNotNull(bookingDtoList.size());
    }

    @Test
    void getAllBookingCurrentUser_Current() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.findByBookerEquals(Mockito.any(User.class)))
                .thenReturn(bookingList);
        List<BookingDto> bookingDtoList = bookingService.getAllBookingCurrentUser(1, "CURRENT",
                Optional.ofNullable(null), Optional.ofNullable(null));
        assertNotNull(bookingDtoList.size());
    }

    @Test
    void getAllBookingCurrentUser_Should_Throw_UnavailableStateException() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        assertThrows(UnavailableStateException.class, () -> bookingService.getAllBookingCurrentUser(1, "AL",
                Optional.ofNullable(null), Optional.ofNullable(null)));
    }

    @Test
    void getAllBookingCurrentOwner_Should_Throw_UnavailableStateException() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findByOwnerId(Mockito.anyInt())).thenReturn(itemsList);
        Mockito.when(bookingRepository.findByItemEquals(Mockito.any(Item.class)))
                .thenReturn(bookingList);
        assertThrows(UnavailableItemException.class, () -> bookingService.getAllBookingCurrentOwner(2, "AL",
                Optional.ofNullable(null), Optional.ofNullable(null)));

    }
}