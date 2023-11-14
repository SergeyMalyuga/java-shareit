package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
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
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Booking addBooking(Booking booking, int bookerId) {
        Optional<Item> optional = itemRepository.findById(booking.getItemId());
        Optional<User> optionalUser = userRepository.findById(bookerId);
        if (optional.isPresent() && optionalUser.isPresent()) {
            Item item = optional.get();
            User user = optionalUser.get();
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
        } else {
            throw new NoDataFoundException("Item с id: " + booking.getItemId() + " не найдена.");
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(int bookingId, int userId) {
        Optional<Booking> optional = bookingRepository.findById(bookingId);
        if (optional.isPresent()) {
            Booking booking = optional.get();
            if (booking.getBooker().getId() == userId || booking.getItem().getOwnerId() == userId) {
                return optional.get();
            } else {
                throw new NoDataFoundException("Booking с id: " + bookingId + " не найден.");
            }
        } else {
            throw new NoDataFoundException("Booking с id: " + bookingId + " не найден.");
        }
    }

    @Override
    public List<Booking> getAllBookingCurrentUser(int userId, String state) {
        Optional<User> optionalUser = userRepository.findById(userId);
        List<Booking> userBookingList = new ArrayList<>();
        if (optionalUser.isPresent()) {
            switch (state) {
                case "ALL":
                    return bookingRepository.findByBookerEquals(optionalUser.get()).stream().sorted((e1, e2) ->
                            e2.getStart().compareTo(e1.getStart())).collect(Collectors.toList());
                case "REJECTED":
                    return bookingRepository.findByBookerEquals(optionalUser.get()).stream()
                            .filter(e -> e.getStatus().toString().equals("REJECTED")).sorted((e1, e2) ->
                                    e2.getStart().compareTo(e1.getStart())).collect(Collectors.toList());
                case "WAITING":
                    return bookingRepository.findByBookerEquals(optionalUser.get()).stream()
                            .filter(e -> e.getStatus().toString().equals("WAITING")).sorted((e1, e2) ->
                                    e2.getStart().compareTo(e1.getStart())).collect(Collectors.toList());
                case "FUTURE":
                    return bookingRepository.findByBookerEquals(optionalUser.get()).stream()
                            .filter(e -> e.getStart().isAfter(LocalDateTime.now())).sorted((e1, e2) ->
                                    e2.getStart().compareTo(e1.getStart())).collect(Collectors.toList());
                case "PAST":
                    return bookingRepository.findByBookerEquals(optionalUser.get()).stream()
                            .filter(e -> e.getEnd().isBefore(LocalDateTime.now())).sorted((e1, e2) ->
                                    e2.getStart().compareTo(e1.getStart())).collect(Collectors.toList());
                case "CURRENT":
                    return bookingRepository.findByBookerEquals(optionalUser.get()).stream()
                            .filter(e -> e.getStart().isBefore(LocalDateTime.now())
                                    && e.getEnd().isAfter(LocalDateTime.now())).sorted((e1, e2) ->
                                    e2.getStart().compareTo(e1.getStart())).collect(Collectors.toList());
                default:
                    throw new UnavailableStateException("Unknown state: UNSUPPORTED_STATUS");
            }
        } else {
            throw new NoDataFoundException("Пользователь с id:" + userId + " не найден.");
        }
    }

    @Override
    public List<Booking> getAllBookingCurrentOwner(int userId, String state) { //TODO
        Optional<User> optionalUser = userRepository.findById(userId);
        List<Booking> ownerBookingList = new ArrayList<>();
        if (optionalUser.isPresent()) {
            List<Item> optionalItem = itemRepository.findByOwnerIdEquals(userId);
            for (Item item : optionalItem) {
                ownerBookingList.addAll(bookingRepository.findByItemEquals(item));
            }
            switch (state) {
                case "ALL":
                    return ownerBookingList.stream().sorted((e1, e2) -> e2.getStart().compareTo(e1.getStart()))
                            .collect(Collectors.toList());
                case "REJECTED":
                    return ownerBookingList.stream().filter(e -> e.getStatus().toString().equals("REJECTED"))
                            .sorted((e1, e2) -> e2.getStart().compareTo(e1.getStart()))
                            .collect(Collectors.toList());
                case "WAITING":
                    return ownerBookingList.stream().filter(e -> e.getStatus().toString().equals("WAITING"))
                            .sorted((e1, e2) -> e2.getStart().compareTo(e1.getStart()))
                            .collect(Collectors.toList());
                case "FUTURE":
                    return ownerBookingList.stream().filter(e -> e.getStart().isAfter(LocalDateTime.now()))
                            .sorted((e1, e2) -> e2.getStart().compareTo(e1.getStart()))
                            .collect(Collectors.toList());
                case "PAST":
                    return ownerBookingList.stream().filter(e -> e.getEnd().isBefore(LocalDateTime.now()))
                            .sorted((e1, e2) -> e2.getStart().compareTo(e1.getStart()))
                            .collect(Collectors.toList());
                case "CURRENT":
                    return ownerBookingList.stream().filter(e -> e.getStart().isBefore(LocalDateTime.now())
                                    && e.getEnd().isAfter(LocalDateTime.now()))
                            .sorted((e1, e2) -> e2.getStart().compareTo(e1.getStart()))
                            .collect(Collectors.toList());
                default:
                    throw new UnavailableItemException("Unknown state: UNSUPPORTED_STATUS");
            }
        } else {
            throw new NoDataFoundException("Пользователь с id:" + userId + " не найден.");
        }
    }

    public Booking getBookingByOwner(int bookingId, int userId) {
        Optional<Booking> optional = bookingRepository.findById(bookingId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optional.isPresent() && optionalUser.isPresent()) {
            if (optionalUser.get().getId() == optional.get().getItem().getOwnerId()) {
                return optional.get();
            } else {
                throw new NoDataFoundException("Подтверждение или отклонение бронирования может осуществлять" +
                        "только владелец вещи.");
            }
        } else {
            throw new NoDataFoundException("Пользователя или владельца вещи не существует.");
        }
    }

    @Override
    public Booking ConfirmationOrRejectionBooking(int bookingId, int userId, String bookingStatus) {
        Booking booking = getBookingByOwner(bookingId, userId);
        switch (bookingStatus) {
            case "true":
                if (booking.getStatus().toString().equals("APPROVED")) {
                    throw new UnavailableItemException("Статус уже был присвоен.");
                }
                booking.setStatus(BookingStatus.APPROVED);
                bookingRepository.save(booking);
                return booking;
            case "false":
                booking.setStatus(BookingStatus.REJECTED);
                bookingRepository.save(booking);
                return booking;
        }
        return booking;
    }
}