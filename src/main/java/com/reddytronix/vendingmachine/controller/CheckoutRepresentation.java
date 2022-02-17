package com.reddytronix.vendingmachine.controller;

import java.util.List;

import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.reddytronix.vendingmachine.model.CoinAvailability;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class CheckoutRepresentation {

    //TODO Separate data representation for input and output is preferred. For simplification, single model is used
    //Request Attributes
    private Long productId;

    @Min(value = 1)
    private Long quantity;

    //Response Attribute
    private ProductRepresentation product;
    private Long totalOrderAmount;
    private Long remainingDepositAmount;
    private List<CoinAvailability> availableCoins;
}
