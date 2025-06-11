package org.expensly.authService.utils.errors;

import org.springframework.http.HttpStatus;

public class ExceptionFactory {
    private static CustomError customError(String message, HttpStatus statusCode, Object object) {
        return CustomError
                .builder()
                .data(object)
                .message(message)
                .statusCode(statusCode)
                .build();
    }

    public static CustomError notFoundException(String message) {
        return customError(message, HttpStatus.NOT_FOUND, null);
    }

    public static CustomError tokenException(String message) {
        return customError(message, HttpStatus.UNAUTHORIZED, null);
    }

    public static CustomError databaseException(String message) {
        return customError(message, HttpStatus.BAD_REQUEST, null);
    }

    public static CustomError oauthCodeException(String message) {
        return customError(message, HttpStatus.UNAUTHORIZED, null);
    }
}
