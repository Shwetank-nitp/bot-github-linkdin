package org.expensly.authService.utils.response;

import java.time.Instant;

public class ResponseFactory {
    public static <T> Response<T> successResponse(String message, T data) {
        return Response.
                <T>builder()
                .data(data)
                .message(message)
                .datetime(Instant.now())
                .status(true)
                .build();
    }

    public static <T> Response<T> failureResponse(String message, T data) {
        return Response.
                <T>builder()
                .data(data)
                .message(message)
                .datetime(Instant.now())
                .status(false)
                .build();
    }
}
