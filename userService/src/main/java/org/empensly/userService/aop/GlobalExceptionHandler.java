package org.empensly.userService.aop;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.empensly.userService.utils.errors.CustomError;
import org.empensly.userService.utils.response.Response;
import org.empensly.userService.utils.response.ResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomError.class)
    public ResponseEntity<Response<Object>> handler(CustomError customError) {
        return new ResponseEntity<>(
                ResponseFactory.failResponse(
                        customError.getMessage(),
                        customError.getData()
                ),
                customError.getStatusCode()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response<?>> constraintValidationHandler(ConstraintViolationException ex) {
        Response<?> res = ResponseFactory.failResponse(
                "constraint violation",
                ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toArray()
        );
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response<?>> httpMessageExceptionHandler(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(
                ResponseFactory.failResponse(
                        "failed to resolve the message type",
                        ex.getMessage()
                ),
                HttpStatus.BAD_REQUEST
        );
    }
}
