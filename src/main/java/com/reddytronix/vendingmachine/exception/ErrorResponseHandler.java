package com.reddytronix.vendingmachine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ErrorResponseHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> exception(ResourceNotFoundException exception) {

        final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        final ErrorResponse errorResponse = ErrorResponse.builder()
                                                         .code(httpStatus.value())
                                                         .status(httpStatus.getReasonPhrase())
                                                         .message(exception.getMessage())
                                                         .build();
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ErrorResponse> exception(BadRequestException exception) {

        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ErrorResponse errorResponse = ErrorResponse.builder()
                                                         .code(httpStatus.value())
                                                         .status(httpStatus.getReasonPhrase())
                                                         .message(exception.getMessage())
                                                         .build();
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorResponse> exception(RuntimeException exception) {

        log.error("Exception handler", exception);
        final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        final ErrorResponse errorResponse = ErrorResponse.builder()
                                                         .code(httpStatus.value())
                                                         .status(httpStatus.getReasonPhrase())
                                                         .message(exception.getMessage())
                                                         .build();
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> exception(MethodArgumentNotValidException exception) {

        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ErrorResponse errorResponse = ErrorResponse.builder()
                                                         .code(httpStatus.value())
                                                         .status(httpStatus.getReasonPhrase())
                                                         .message(exception.getMessage())
                                                         .build();
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
