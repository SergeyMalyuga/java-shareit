package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(Booking booking, int bookerId);

    BookingDto getBookingById(int bookingId, int userId);

    List<BookingDto> getAllBookingCurrentUser(int userId, String state);

    List<BookingDto> getAllBookingCurrentOwner(int userId, String state);

    BookingDto confirmationOrRejectionBooking(int bookingId, int userId, String bookingStatus);
}
