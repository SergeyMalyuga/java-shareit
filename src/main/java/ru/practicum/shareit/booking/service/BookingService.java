package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    BookingDto addBooking(Booking booking, int bookerId);

    BookingDto getBookingById(int bookingId, int userId);

    List<BookingDto> getAllBookingCurrentUser(int userId, String state, Optional<Integer> from, Optional<Integer> size);

    List<BookingDto> getAllBookingCurrentOwner(int userId, String state, Optional<Integer> from, Optional<Integer> size);

    BookingDto confirmationOrRejectionBooking(int bookingId, int userId, String bookingStatus);

    Booking getBookingByOwner(int bookingId, int userId);
}
