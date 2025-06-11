package org.expensly.authService.utils.errors;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomError extends RuntimeException {
    private final HttpStatus statusCode;
    private final Object data;

    @Builder
    public CustomError(String message, HttpStatus statusCode, Object data) {
        super(message);
        this.statusCode = statusCode;
        this.data = data;
    }
}
