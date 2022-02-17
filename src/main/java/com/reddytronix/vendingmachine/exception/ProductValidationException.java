package com.reddytronix.vendingmachine.exception;

public class ProductValidationException extends BadRequestException {
    public ProductValidationException(String message) {
        super(message);
    }
}
