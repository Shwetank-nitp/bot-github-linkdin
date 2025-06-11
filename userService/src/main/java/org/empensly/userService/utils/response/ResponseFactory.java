package org.empensly.userService.utils.response;

import java.time.Instant;

public class ResponseFactory {
    public static <T> Response<T> successResponse(String message, T data) {
        return Response.<T>builder()
                .data(data)
                .message(message)
                .status(true)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> Response<T> failResponse(String message, T data) {
        return Response.<T>builder()
                .data(data)
                .message(message)
                .status(false)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> Response<T> failResponse(String message) {
        return Response.<T>builder()
                .data(null)
                .message(message)
                .status(false)
                .timestamp(Instant.now())
                .build();
    }

}
