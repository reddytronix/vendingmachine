package com.reddytronix.vendingmachine.validation;

import org.springframework.stereotype.Component;

import com.reddytronix.vendingmachine.controller.ProductRepresentation;
import com.reddytronix.vendingmachine.exception.ProductValidationException;

@Component
public class ProductValidator {

   public void validate(final ProductRepresentation productRepresentation) {

        if (productRepresentation.getAmountAvailable() <= 0) {
            throw new ProductValidationException("Product amount available is " + productRepresentation.getAmountAvailable() + ", it should be greater > 0");
        }

        if (productRepresentation.getCost() <= 0 || productRepresentation.getCost() % 5 != 0) {
            throw new ProductValidationException("Product cost is " + productRepresentation.getCost() + ", it should "
                                                 + "be greater > 0 and multiple of 5");
        }

    }
}
