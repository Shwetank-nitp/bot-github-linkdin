package org.empensly.userService.utils.errors;

import lombok.*;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomError extends RuntimeException {
    private HttpStatus statusCode;
    private Object data;

    @Builder
    public CustomError(String message, HttpStatus statusCode, Object data) {
        super(message);
        this.statusCode = statusCode;
        this.data = data;
    }
}
