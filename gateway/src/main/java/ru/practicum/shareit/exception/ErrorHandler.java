package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse noDataFound(NoDataFoundException e) {
        return new ErrorResponse(e.getMessage(), "Данные не найдены.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse emailDuplicate(EmailDuplicateException e) {
        return new ErrorResponse(e.getMessage(), "Данный email уже существует.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse unavailableItem(UnavailableItemException e) {
        return new ErrorResponse(e.getMessage(), "Данная вещь не доступна для бронирования.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse unavailableState(UnavailableStateException e) {
        return new ErrorResponse(e.getMessage(), "Не верный статус.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse postWithoutBooking(PostWithoutBookingException e) {
        return new ErrorResponse(e.getMessage(), "Пользователь не может оставить комментарий к данной вещи.");
    }

}
