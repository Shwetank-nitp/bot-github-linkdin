package org.expensly.authService.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class Response<T> {
    private String message;
    private T data;
    private boolean status;
    private Instant datetime;
}
