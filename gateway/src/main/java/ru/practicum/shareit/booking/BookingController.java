package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.UnavailableStateException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> postBooking(@RequestBody BookItemRequestDto bookItemRequestDto,
                                              @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingClient.postBooking(userId, bookItemRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> confirmationOrRejectionBooking(@PathVariable int bookingId,
                                                                 @RequestHeader("X-Sharer-User-Id") int userId,
                                                                 @RequestParam(name = "approved")
                                                                 String bookingStatus) {

        return bookingClient.confirmationOrRejectionBooking(bookingId, userId, bookingStatus);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") int userId,
                                                 @PathVariable int bookingId) {
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingCurrentUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                           @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                           Integer from,
                                                           @Positive @RequestParam(name = "size", defaultValue = "10")
                                                           Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new UnavailableStateException("Unknown state: UNSUPPORTED_STATUS"));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getAllBookingCurrentUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingCurrentOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                            @RequestParam(name = "state", defaultValue = "ALL")
                                                            String stateParam,
                                                            @PositiveOrZero @RequestParam(name = "from",
                                                                    defaultValue = "0") Integer from,
                                                            @Positive @RequestParam(name = "size",
                                                                    defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new UnavailableStateException("Unknown state: UNSUPPORTED_STATUS"));
        return bookingClient.getAllBookingCurrentOwner(userId, state, from, size);
    }
}