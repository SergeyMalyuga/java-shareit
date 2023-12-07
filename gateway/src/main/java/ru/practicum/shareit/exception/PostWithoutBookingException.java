package ru.practicum.shareit.exception;

public class PostWithoutBookingException extends RuntimeException {

    public PostWithoutBookingException(String message) {
        super(message);
    }
}
