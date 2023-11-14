package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;

import java.util.List;

public interface BookingService {
    Booking addBooking(Booking booking, int bookerId);

    Booking getBookingById(int bookingId, int userId);

    List<Booking> getAllBookingCurrentUser(int userId, String state);

    List<Booking> getAllBookingCurrentOwner(int userId, String state);

    Booking confirmationOrRejectionBooking(int bookingId, int userId, String bookingStatus);
}
