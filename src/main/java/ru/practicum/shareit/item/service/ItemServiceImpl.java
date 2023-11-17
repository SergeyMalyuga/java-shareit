package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.exception.NoDataFoundException;
import ru.practicum.shareit.exception.PostWithoutBookingException;
import ru.practicum.shareit.exception.UnavailableItemException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public ItemDto addItem(int userId, Item item) {
        if (userRepository.findById(userId).isPresent()) {
            item.setOwnerId(userId);
            itemRepository.save(item);
            return itemMapper.itemDto(item);
        } else {
            throw new NoDataFoundException("Пользователь с id:" + userId + " не найден.");
        }
    }

    @Override
    public ItemDto updateItem(int userId, int itemId, Map<Object, Object> fields) {
        Item item = getItemById(itemId, userId);
        if (item.getOwnerId() == userId) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Item.class, (String) key);
                field.setAccessible(true);
                if (!((String) key).equalsIgnoreCase("id")
                        && !((String) key).equalsIgnoreCase("ownerId")) {
                    ReflectionUtils.setField(field, item, value);
                }
            });
            itemRepository.save(item);
            return itemMapper.itemDto(item);
        } else {
            throw new NoDataFoundException("Пользователь с id:" + userId
                    + " не является владельцем данной вещи. Изменения не сохранены.");
        }
    }

    @Override
    public ItemDto getItemByIdDto(int itemId, int userId) {
        Optional<Item> optional = itemRepository.findById(itemId);
        List<Comment> commentsList = commentRepository.getCommentByItem(itemId);
        for (Comment comment : commentsList) {
            comment.setAuthorName(comment.getAuthor().getName());
        }
        if (optional.isPresent()) {
            Item item = optional.get();
            ItemDto itemDto = itemMapper.itemDto(optional.get());
            if (itemDto.getOwnerId() == userId) {
                itemDto = addBookingToItem(item, itemDto);
            }
            itemDto.setComments(commentsList.stream().map(e -> commentMapper.toCommentDto(e))
                    .collect(Collectors.toList()));
            return itemDto;
        } else {
            throw new NoDataFoundException("Item с id: " + itemId + " не найдена.");
        }
    }

    private ItemDto addBookingToItem(Item item, ItemDto itemDto) {
        if (!item.getBookingList().isEmpty()) {
            List<Booking> bookingListNext = item.getBookingList().stream()
                    .filter(e -> e.getStart().isAfter(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Booking::getStart)).collect(Collectors.toList());
            List<Booking> bookingListLast = item.getBookingList().stream()
                    .filter(e -> e.getEnd().isBefore(LocalDateTime.now()))
                    .sorted((e1, e2) -> e2.getEnd().compareTo(e1.getEnd())).collect(Collectors.toList());
            List<Booking> bookingListLastAfterNow = item.getBookingList().stream()
                    .filter(e -> e.getEnd().isAfter(LocalDateTime.now()) && !e.getStatus().toString().equals("REJECTED")).collect(Collectors.toList());
            if (bookingListLast.size() >= 1 && bookingListNext.size() >= 1) {
                itemDto.setLastBooking(bookingMapper.bookingDto(bookingListLast.get(0)));
                itemDto.getLastBooking().setBookerId(bookingListLast.get(0).getBooker().getId());
                itemDto.setNextBooking(bookingMapper.bookingDto(bookingListNext.get(0)));
                itemDto.getNextBooking().setBookerId(bookingListNext.get(0).getBooker().getId());
            } else if (bookingListLast.size() == 0 && bookingListLastAfterNow.size() >= 1) {
                itemDto.setLastBooking(bookingMapper.bookingDto(bookingListLastAfterNow.get(0)));
                itemDto.getLastBooking().setBookerId(bookingListLastAfterNow.get(0).getBooker().getId());
            }
        }
        return itemDto;
    }

    @Override
    public Item getItemById(int itemId, int userId) {
        Optional<Item> optional = itemRepository.findById(itemId);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new NoDataFoundException("Item с id: " + itemId + " не найдена.");
        }
    }

    @Override
    public List<ItemDto> getAllItemForOwner(int ownerId) {
        List<Item> itemList = itemRepository.findByOwnerId(ownerId).stream()
                .sorted(Comparator.comparingInt(Item::getId)).collect(Collectors.toList());
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemList) {
            ItemDto itemDto = itemMapper.itemDto(item);
            itemDtoList.add(addBookingToItem(item, itemDto));
        }
        return itemDtoList;
    }

    @Override
    public List<ItemDto> searchItem(String request) {
        Set<Item> itemsList = new HashSet<>();
        if (request.isBlank()) {
            return new ArrayList<>();
        }
        itemsList.addAll(itemRepository.findByNameIgnoreCaseContaining(request));
        itemsList.addAll(itemRepository.findByDescriptionIgnoreCaseContaining(request));
        List<ItemDto> itemsListSorted = itemsList.stream().filter(e -> e.getAvailable() == true)
                .sorted(Comparator.comparing(Item::getName))
                .map(e -> itemMapper.itemDto(e)).collect(Collectors.toList());
        return itemsListSorted;
    }

    @Override
    public CommentDto addComment(int itemId, int userId, Comment comment) {
        ItemDto itemDto = getItemByIdDto(itemId, userId);
        Item item = getItemById(itemId, userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (comment.getText().isBlank()) {
                throw new UnavailableItemException("Комментарий не может быть пустым.");
            } else {
                comment.setItem(item);
                comment.setAuthor(user);
                comment.setAuthorName(user.getName());
                comment.setCreated(LocalDateTime.now());
            }
            List<Booking> itemListBooking = bookingRepository.findByBookerEquals(user).stream()
                    .filter(e -> e.getItem().getId() == itemId && !e.getStatus().toString()
                            .equals("REJECTED") && !e.getStart().isAfter(LocalDateTime.now())).collect(Collectors.toList());
            if (!itemListBooking.isEmpty()) {
                itemDto.getComments().add(commentMapper.toCommentDto(comment));
            } else {
                throw new PostWithoutBookingException("Пользователь с id:" + userId
                        + " не может оставить комментарий, " +
                        "т.к. не брал " + itemDto.getName() + " вещь в аренду.");
            }
        } else {
            throw new NoDataFoundException("Пользователь с id:" + userId + " не найден.");
        }
        Comment userItemComment = commentRepository.getUserCommentByItem(userId, itemId);
        if (userItemComment != null) {
            comment.setId(userItemComment.getId());
        }
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }
}
