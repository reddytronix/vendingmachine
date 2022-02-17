package com.reddytronix.vendingmachine.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Checkout {
    private Long productId;
    private Long quantity;

    private Product product;
    private Long totalOrderAmount;
    private Long remainingDepositAmount;
    private List<CoinAvailability> availableCoins;
}
