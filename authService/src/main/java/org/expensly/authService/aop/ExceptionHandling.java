package org.expensly.authService.aop;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.expensly.authService.utils.errors.CustomError;
import org.expensly.authService.utils.response.Response;
import org.expensly.authService.utils.response.ResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandling {
    @ExceptionHandler(CustomError.class)
    public ResponseEntity<Response<Object>> handler(CustomError customError) {
        return new ResponseEntity<>(
                ResponseFactory.failureResponse(
                        customError.getMessage(),
                        customError.getData()
                ),
                customError.getStatusCode()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response<?>> constraintValidationHandler(ConstraintViolationException ex) {
        Response<?> res = ResponseFactory.failureResponse(
                "constraint violation",
                ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toArray()
        );
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response<?>> httpMessageExceptionHandler(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(
                ResponseFactory.failureResponse(
                        "failed to resolve the message type",
                        ex.getMessage()
                ),
                HttpStatus.BAD_REQUEST
        );
    }
}
