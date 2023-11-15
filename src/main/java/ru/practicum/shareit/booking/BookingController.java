package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
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
    public BookingDto addBooking(@RequestBody Booking booking, @RequestHeader("X-Sharer-User-Id") int userId) {
        // userService.getUserById(userId);
        return bookingService.addBooking(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto confirmationOrRejectionBooking(@PathVariable int bookingId,
                                                     @RequestHeader("X-Sharer-User-Id") int userId,
                                                     @RequestParam(name = "approved") String bookingStatus) {
        return bookingService.confirmationOrRejectionBooking(bookingId, userId, bookingStatus);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int bookingId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping()
    public List<BookingDto> getAllBookingCurrentUser(@RequestHeader("X-Sharer-User-Id") int userId,
                                                     @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.getAllBookingCurrentUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingCurrentOwner(@RequestHeader("X-Sharer-User-Id") int userId,
                                                      @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.getAllBookingCurrentOwner(userId, state);
    }

}
