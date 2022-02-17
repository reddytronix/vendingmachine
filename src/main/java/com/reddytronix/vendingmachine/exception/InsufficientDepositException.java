package com.reddytronix.vendingmachine.exception;

public class InsufficientDepositException extends BadRequestException {

    public InsufficientDepositException(String message) {
        super(message);
    }
}
