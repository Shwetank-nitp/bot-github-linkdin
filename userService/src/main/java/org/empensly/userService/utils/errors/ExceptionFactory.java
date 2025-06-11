package org.empensly.userService.utils.errors;

import org.springframework.http.HttpStatus;

public class ExceptionFactory {
    private static CustomError customError(String message, HttpStatus statusCode, Object data) {
        return CustomError.builder()
                .data(data)
                .message(message)
                .statusCode(statusCode)
                .build();
    }

    public static CustomError notFoundException(String message) {
        return customError(message, HttpStatus.NOT_FOUND, null);
    }
}
