package com.reddytronix.vendingmachine.exception;

public class UserUnauthorisedException extends BadRequestException {

    public UserUnauthorisedException(String message) {
        super(message);
    }
}
