package com.reddytronix.vendingmachine.exception;

public class InvalidDepositException extends BadRequestException {

    public InvalidDepositException(String message) {
        super(message);
    }
}
