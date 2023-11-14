package ru.practicum.shareit.exception;

public class UnavailableStateException extends RuntimeException {
    public UnavailableStateException(String message) {
        super(message);
    }
}
