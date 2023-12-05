package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ErrorResponseTest {

    @Test
    void getError() {
        ErrorResponse errorResponse = new ErrorResponse("ошибка", "error");
        String error = errorResponse.getError();
        assertNotNull(error);
    }

    @Test
    void getDescription() {
        ErrorResponse errorResponse = new ErrorResponse("ошибка", "error");
        String error = errorResponse.getDescription();
        assertNotNull(error);
    }

    @Test
    void setError() {
        ErrorResponse errorResponse = new ErrorResponse("ошибка", "error");
        errorResponse.setError("new error");
        String error = errorResponse.getError();
        assertNotNull(error);
    }

    @Test
    void setDescription() {
        ErrorResponse errorResponse = new ErrorResponse("ошибка", "error");
        errorResponse.setDescription("new description");
        String error = errorResponse.getError();
        assertNotNull(error);
    }
}