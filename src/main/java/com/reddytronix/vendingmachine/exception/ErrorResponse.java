package com.reddytronix.vendingmachine.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {
    private final int code;
    private final String status;
    private final String message;
}
