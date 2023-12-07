package ru.practicum.shareit.item.service;

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
import ru.practicum.shareit.exception.NoDataFoundException;
import ru.practicum.shareit.exception.PostWithoutBookingException;
import ru.practicum.shareit.exception.UnavailableItemException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemDtoMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDtoMapper;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @InjectMocks
    private ItemService itemService = new ItemServiceImpl();
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private ItemDtoMapper itemDtoMapper;
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private BookingMapper bookingMapper;
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private CommentMapper commentMapper;
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private ItemMapper itemMapper;
    private Item item;
    private Item item2;
    private User user;
    private User user2;
    private Booking booking;
    private Comment comment;
    private Comment comment2;
    private CommentDto commentDto;
    private List<Item> itemList = new ArrayList<>();
    private List<ItemDto> itemsDtoList = new ArrayList<>();
    private List<Booking> bookingList = new ArrayList<>();
    private PageableExecutionUtils pageUtils;

    @BeforeEach
    private void setUp() {
        user = new User().setId(1).setEmail("serg@mail.ru").setName("Sergey");
        user2 = new User().setId(2).setEmail("galina@mail.ru").setName("Galina");
        item = new Item().setId(1).setAvailable(true).setDescription("Аккумуляторная дрель")
                .setName("Дрель").setOwnerId(1);
        item2 = new Item().setId(2).setAvailable(true).setDescription("Ручной интсрумент").setName("Молоток")
                .setOwnerId(1).setRequestId(2);
        itemDtoMapper = new ItemDtoMapper();
        comment = new Comment().setText("Отличный молоток");
        comment2 = new Comment().setId(1).setItem(item2)
                .setCreated(LocalDateTime.now()).setText("   ");
        commentDto = new CommentDto().setId(1).setItem(itemDtoMapper.itemDto(item2))
                .setCreated(LocalDateTime.now()).setText("Отличный молоток");
        Collections.addAll(itemList, item);
        Collections.addAll(itemsDtoList, itemDtoMapper.itemDto(item));
        booking = new Booking().setId(1).setStatus(BookingStatus.WAITING).setBooker(user2).setItem(item)
                .setStart(LocalDateTime.now()).setEnd(LocalDateTime.now()
                        .plusHours(1)).setItemId(1).setBookerId(2);
        bookingList.add(booking);
        commentMapper.setItemDtoMapper(new ItemDtoMapper());
        commentMapper.setUserDtoMapper(new UserDtoMapper());
    }

    @Test
    void addItem_Should_Return_Item() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        ItemDto itemDto = itemService.addItem(1, itemDtoMapper.itemDto(item));
        assertEquals(user.getId(), itemDto.getOwnerId());
    }

    @Test
    void addItem_Should_Throw_NoDataFoundException() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenAnswer(invocationOnMock -> {
            int userId = invocationOnMock.getArgument(0, Integer.class);
            if (userId == 0) {
                throw new NoDataFoundException("");
            }
            throw new NoDataFoundException("");
        });
        assertThrows(NoDataFoundException.class, () -> itemService.addItem(0, itemDtoMapper.itemDto(item)));

    }

    @Test
    void updateItem_Should_Return_updateItem() {
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        Map<Object, Object> fields = new HashMap<>();
        fields.put("name", "Лопата");
        ItemDto itemDto = itemService.updateItem(1, 1, fields);
        assertEquals("Лопата", itemDto.getName());
    }

    @Test
    void updateItem_Should_Throw_NoDataFoundException() {
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        Map<Object, Object> fields = new HashMap<>();
        fields.put("name", "Лопата");
        assertThrows(NoDataFoundException.class, () -> itemService.updateItem(2, 1, fields));
    }

    @Test
    void getItemByIdDto_Should_Return_ItemDto() {
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        ItemDto itemDto = itemService.getItemByIdDto(1, 1);
        assertNotNull(itemDto);
        assertEquals(1, itemDto.getId());
    }

    @Test
    void getItemByIdDto_Should_Throw_NoDataFoundException() {
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenAnswer(invocationOnMock -> {
            int itemId = invocationOnMock.getArgument(0, Integer.class);
            if (itemId == 0) {
                throw new NoDataFoundException("Item не найдена.");
            }
            throw new NoDataFoundException("Item не найдена.");
        });
        assertThrows(NoDataFoundException.class, () -> itemService.getItemByIdDto(1, 1));
    }


    @Test
    void getItemById_Should_Return_Item() {
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        ItemDto itemDto = itemService.getItemByIdDto(1, 1);
        assertNotNull(itemDto);
        assertEquals(1, itemDto.getId());
    }

    @Test
    void getItemById_Should_Throw_NoDataFoundException() {
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenAnswer(invocationOnMock -> {
            int itemId = invocationOnMock.getArgument(0, Integer.class);
            if (itemId == 0) {
                throw new NoDataFoundException("Item не найдена.");
            }
            throw new NoDataFoundException("Item не найдена.");
        });
        assertThrows(NoDataFoundException.class, () -> itemService.getItemById(1, 1));
    }

    @Test
    void getAllItemForOwner_Should_Return_Item_WithPagination() {
        Page<Item> page = PageableExecutionUtils.getPage(itemList, PageRequest.of(0, 3),
                () -> itemList.size());
        Mockito.when(itemRepository.findByOwnerId(Mockito.anyInt(), Mockito.any(Pageable.class))).thenReturn(page);
        List<ItemDto> itemDtoList = itemService.getAllItemForOwner(1, Optional.of(0),
                Optional.of(3));
        assertNotNull(itemDtoList);
        assertEquals(1, itemDtoList.size());
    }

    @Test
    void getAllItemForOwner_Should_Throw_UnavailableItemException() {
        Page<Item> page = PageableExecutionUtils.getPage(itemList, PageRequest.of(0, 3),
                () -> itemList.size());
        assertThrows(UnavailableItemException.class, () -> itemService.getAllItemForOwner(1,
                Optional.of(0), Optional.of(-3)));
    }

    @Test
    void getAllItemForOwner_Should_Return_Item_Without_Pagination() {
        Page<Item> page = PageableExecutionUtils.getPage(itemList, PageRequest.of(0, 3),
                () -> itemList.size());
        Mockito.when(itemRepository.findByOwnerId(Mockito.anyInt())).thenReturn(itemList);
        List<ItemDto> itemDtoList = itemService.getAllItemForOwner(1, Optional.ofNullable(null),
                Optional.ofNullable(null));
        assertNotNull(itemDtoList);
        assertEquals(1, itemDtoList.size());
    }

    @Test
    void searchItem_Should_Return_ItemDtoList_With_Pagination() {
        Page<Item> page = PageableExecutionUtils.getPage(itemList, PageRequest.of(0, 3),
                () -> itemList.size());
        Mockito.when(itemRepository.findByNameOrDescriptionWithPagination(Mockito.anyString(),
                Mockito.any(Pageable.class))).thenReturn(page);
        List<ItemDto> itemDtoList = itemService.searchItem("Дрель", Optional.of(0),
                Optional.of(3));
        assertEquals(1, itemDtoList.size());
        assertEquals("Дрель", itemDtoList.get(0).getName());
    }

    @Test
    void searchItem_Should_Return_ItemDtoList_Without_Pagination() {
        Page<Item> page = PageableExecutionUtils.getPage(itemList, PageRequest.of(0, 3),
                () -> itemList.size());
        Mockito.when(itemRepository.findByNameIgnoreCaseContaining(Mockito.anyString())).thenReturn(itemList);
        Mockito.when(itemRepository.findByDescriptionIgnoreCaseContaining(Mockito.anyString())).thenReturn(itemList);
        List<ItemDto> itemDtoList = itemService.searchItem("Дрель", Optional.ofNullable(null),
                Optional.ofNullable(null));
        assertEquals(1, itemDtoList.size());
        assertEquals("Дрель", itemDtoList.get(0).getName());
    }

    @Test
    void searchItem_Should_Throw_UnavailableItemException() {
        Page<Item> page = PageableExecutionUtils.getPage(itemList, PageRequest.of(0, 3),
                () -> itemList.size());
        assertThrows(UnavailableItemException.class, () -> itemService.searchItem("Дрель",
                Optional.ofNullable(-1), Optional.ofNullable(-1)));
    }

    @Test
    void addComment() {
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user2));
        Mockito.when(bookingRepository.findByBookerEquals(Mockito.any(User.class))).thenReturn(bookingList);
        CommentDto newCommentDto = itemService.addComment(1, 2, comment);
        assertNotNull(newCommentDto);
        assertEquals(1, comment.getItem().getId());
    }

    @Test
    void addComment_Should_Throw_PostWithoutBookingException() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.findByBookerEquals(Mockito.any(User.class))).thenReturn(new ArrayList<>());
        assertThrows(PostWithoutBookingException.class, () -> itemService.addComment(1, 3, comment));
    }

    @Test
    void addComment_Should_Throw_NoDataFoundException() {
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        assertThrows(NoDataFoundException.class, () -> itemService.addComment(1, 3, comment));
    }

    @Test
    void addComment_Should_Throw_UnavailableItemException() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        assertThrows(UnavailableItemException.class, () -> itemService.addComment(1, 3, comment2));
    }
}