package ru.practicum.shareit.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorHandlerTest {
    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void noDataFound_Should_Return_ErrorMessage() {
        String error = errorHandler.noDataFound(new NoDataFoundException("error")).getDescription();
        assertEquals("Данные не найдены.", error);
    }

    @Test
    void emailDuplicate_Should_Return_ErrorMessage() {
        String error = errorHandler.emailDuplicate(new EmailDuplicateException("error")).getDescription();
        assertEquals("Данный email уже существует.", error);
    }

    @Test
    void unavailableItem_Should_Return_ErrorMessage() {
        String error = errorHandler.unavailableItem(new UnavailableItemException("error")).getDescription();
        assertEquals("Данная вещь не доступна для бронирования.", error);
    }

    @Test
    void unavailableState_Should_Return_ErrorMessage() {
        String error = errorHandler.unavailableState(new UnavailableStateException("error")).getDescription();
        assertEquals("Не верный статус.", error);
    }

    @Test
    void postWithoutBooking_Should_Return_ErrorMessage() {
        String error = errorHandler.postWithoutBooking(new PostWithoutBookingException("error")).getDescription();
        assertEquals("Пользователь не может оставить комментарий к данной вещи.", error);
    }
}