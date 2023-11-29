package ru.practicum.shareit.booking.service;

import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NoDataFoundException;
import ru.practicum.shareit.exception.UnavailableItemException;
import ru.practicum.shareit.exception.UnavailableStateException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Accessors(chain = true)
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingMapper bookingMapper;

    @Override
    public BookingDto addBooking(Booking booking, int bookerId) {
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new NoDataFoundException("Item с id: " + booking.getItemId() + " не найдена."));
        User user = userRepository.findById(bookerId)
                .orElseThrow(() -> new NoDataFoundException("Пользователь с id:" + bookerId + " не найден."));
        booking.setBooker(user);
        booking.setItem(item);
        item.getBookingList().add(booking);
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        if (item.getOwnerId() == bookerId) {
            throw new NoDataFoundException("Владелец вещи не может разместить заказ на свою же вещь.");
        }
        if (item.getAvailable().toString().equals("true") && start != null && end != null && start.isBefore(end)
                && end.isAfter(start) && !start.isBefore(LocalDateTime.now())) {
            booking.setStatus(BookingStatus.WAITING);
        } else {
            throw new UnavailableItemException(item.getName() + " статус " + item.getAvailable());
        }
        bookingRepository.save(booking);
        return bookingMapper.bookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoDataFoundException("Booking с id: " + bookingId + " не найден."));
        if (booking.getBooker().getId() == userId || booking.getItem().getOwnerId() == userId) {
            return bookingMapper.bookingDto(booking);
        } else {
            throw new NoDataFoundException("Booking с id: " + bookingId + " не найден.");
        }
    }

    @Override
    public List<BookingDto> getAllBookingCurrentUser(int userId, String state, Optional<Integer> from,
                                                     Optional<Integer> size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoDataFoundException("Пользователь с id:" + userId + " не найден."));
        switch (state) {
            case "ALL":
                if (from.isPresent() && size.isPresent()) {
                    if (from.isPresent() && from.get() >= 0 && size.isPresent() && size.get() > 0) {
                        return bookingRepository.findByBookerIdWithPagination(userId,
                                        PageRequest.of((int) Math.ceil((double) from.get() / size.get()),
                                                size.get())).getContent()
                                .stream().sorted((e1, e2) -> e2.getStart().compareTo(e1.getStart()))
                                .map(e -> bookingMapper.bookingDto(e))
                                .collect(Collectors.toList());
                    } else {
                        throw new UnavailableItemException("Не допустимое значение.");
                    }
                } else {
                    return bookingRepository.findByBookerEquals(user).stream().sorted((e1, e2) ->
                                    e2.getStart().compareTo(e1.getStart())).map(e -> bookingMapper.bookingDto(e))
                            .collect(Collectors.toList());
                }
            case "REJECTED":
                return bookingRepository.findByBookerEquals(user).stream()
                        .filter(e -> e.getStatus().toString().equals("REJECTED")).sorted((e1, e2) ->
                                e2.getStart().compareTo(e1.getStart())).map(e -> bookingMapper.bookingDto(e))
                        .collect(Collectors.toList());
            case "WAITING":
                return bookingRepository.findByBookerEquals(user).stream()
                        .filter(e -> e.getStatus().toString().equals("WAITING")).sorted((e1, e2) ->
                                e2.getStart().compareTo(e1.getStart())).map(e -> bookingMapper.bookingDto(e))
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findByBookerEquals(user).stream()
                        .filter(e -> e.getStart().isAfter(LocalDateTime.now())).sorted((e1, e2) ->
                                e2.getStart().compareTo(e1.getStart())).map(e -> bookingMapper.bookingDto(e))
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findByBookerEquals(user).stream()
                        .filter(e -> e.getEnd().isBefore(LocalDateTime.now())).sorted((e1, e2) ->
                                e2.getStart().compareTo(e1.getStart())).map(e -> bookingMapper.bookingDto(e))
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.findByBookerEquals(user).stream()
                        .filter(e -> e.getStart().isBefore(LocalDateTime.now())
                                && e.getEnd().isAfter(LocalDateTime.now())).sorted((e1, e2) ->
                                e2.getStart().compareTo(e1.getStart())).map(e -> bookingMapper.bookingDto(e))
                        .collect(Collectors.toList());
            default:
                throw new UnavailableStateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingDto> getAllBookingCurrentOwner(int userId, String state,
                                                      Optional<Integer> from, Optional<Integer> size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoDataFoundException("Пользователь с id:" + userId + " не найден."));
        List<Booking> ownerBookingList = new ArrayList<>();
        List<Item> listItem = itemRepository.findByOwnerId(userId);
        for (Item item : listItem) {
            ownerBookingList.addAll(bookingRepository.findByItemEquals(item));
        }
        switch (state) {
            case "ALL":
                if (from.isPresent() && size.isPresent()) {
                    if (from.isPresent() && from.get() >= 0 && size.isPresent() && size.get() > 0) {
                        return bookingRepository.findByItemOwnerId(userId,
                                        PageRequest.of((int) Math.ceil((double) from.get() / size.get()),
                                                size.get(), Sort.by("id").descending()))
                                .getContent().stream()
                                .map(e -> bookingMapper.bookingDto(e)).collect(Collectors.toList());
                    } else {
                        throw new UnavailableItemException("Не допустимое значение.");
                    }
                } else {
                    return ownerBookingList.stream().sorted((e1, e2) -> e2.getStart().compareTo(e1.getStart()))
                            .map(e -> bookingMapper.bookingDto(e)).collect(Collectors.toList());
                }
            case "REJECTED":
                return ownerBookingList.stream().filter(e -> e.getStatus().toString().equals("REJECTED"))
                        .sorted((e1, e2) -> e2.getStart().compareTo(e1.getStart()))
                        .map(e -> bookingMapper.bookingDto(e)).collect(Collectors.toList());
            case "WAITING":
                return ownerBookingList.stream().filter(e -> e.getStatus().toString().equals("WAITING"))
                        .sorted((e1, e2) -> e2.getStart().compareTo(e1.getStart()))
                        .map(e -> bookingMapper.bookingDto(e)).collect(Collectors.toList());
            case "FUTURE":
                return ownerBookingList.stream().filter(e -> e.getStart().isAfter(LocalDateTime.now()))
                        .sorted((e1, e2) -> e2.getStart().compareTo(e1.getStart()))
                        .map(e -> bookingMapper.bookingDto(e)).collect(Collectors.toList());
            case "PAST":
                return ownerBookingList.stream().filter(e -> e.getEnd().isBefore(LocalDateTime.now()))
                        .sorted((e1, e2) -> e2.getStart().compareTo(e1.getStart()))
                        .map(e -> bookingMapper.bookingDto(e)).collect(Collectors.toList());
            case "CURRENT":
                return ownerBookingList.stream().filter(e -> e.getStart().isBefore(LocalDateTime.now())
                                && e.getEnd().isAfter(LocalDateTime.now()))
                        .sorted((e1, e2) -> e2.getStart().compareTo(e1.getStart()))
                        .map(e -> bookingMapper.bookingDto(e)).collect(Collectors.toList());
            default:
                throw new UnavailableItemException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    public Booking getBookingByOwner(int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoDataFoundException("Booking с id: " + bookingId + " не найден."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoDataFoundException("Пользователь с id:" + userId + " не найден."));
        if (user.getId() == booking.getItem().getOwnerId()) {
            return booking;
        } else {
            throw new NoDataFoundException("Подтверждение или отклонение бронирования может осуществлять" +
                    "только владелец вещи.");
        }
    }

    @Override
    public BookingDto confirmationOrRejectionBooking(int bookingId, int userId, String bookingStatus) {
        Booking booking = getBookingByOwner(bookingId, userId);
        switch (bookingStatus) {
            case "true":
                if (booking.getStatus().toString().equals("APPROVED")) {
                    throw new UnavailableItemException("Статус уже был присвоен.");
                }
                booking.setStatus(BookingStatus.APPROVED);
                bookingRepository.save(booking);
                return bookingMapper.bookingDto(booking);
            case "false":
                booking.setStatus(BookingStatus.REJECTED);
                bookingRepository.save(booking);
                return bookingMapper.bookingDto(booking);
        }
        return bookingMapper.bookingDto(booking);
    }
}