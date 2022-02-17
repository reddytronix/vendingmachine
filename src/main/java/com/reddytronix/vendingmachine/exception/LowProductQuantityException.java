package com.reddytronix.vendingmachine.exception;

public class LowProductQuantityException extends BadRequestException {

    public LowProductQuantityException(String message) {
        super(message);
    }
}
