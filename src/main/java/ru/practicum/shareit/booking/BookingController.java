package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;

    @PostMapping
    public Booking addBooking(@RequestBody Booking booking, @RequestHeader("X-Sharer-User-Id") int userId) {
        userService.getUserById(userId);
        return bookingService.addBooking(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public Booking confirmationOrRejectionBooking(@PathVariable int bookingId,
                                                  @RequestHeader("X-Sharer-User-Id") int userId,
                                                  @RequestParam(name = "approved") String bookingStatus) {
        return bookingService.сonfirmationOrRejectionBooking(bookingId, userId, bookingStatus);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int bookingId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping()
    public List<Booking> getAllBookingCurrentUser(@RequestHeader("X-Sharer-User-Id") int userId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.getAllBookingCurrentUser(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getAllBookingCurrentOwner(@RequestHeader("X-Sharer-User-Id") int userId,
                                                   @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.getAllBookingCurrentOwner(userId, state);
    }

}
