package com.reddytronix.vendingmachine.utils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.reddytronix.vendingmachine.model.CoinAvailability;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoinUtils {
    public static final int[] SUPPORTED_COINS = { 5, 10, 20, 50, 100 };

    public static List<CoinAvailability> getCoinAvailability(long remainingDepositAmount) {
        final Map<Integer, Integer> availableCoins = new TreeMap<>();
        long remainingChange = remainingDepositAmount;
        for (int i = SUPPORTED_COINS.length - 1; i >= 0; i--) {
            int coinValue = SUPPORTED_COINS[i];

            final int numOfCoins = (int) (remainingChange / coinValue);
            if (numOfCoins >= 1) {
                remainingChange = remainingChange % coinValue;
            }
            availableCoins.put(coinValue, numOfCoins);

        }
        return availableCoins.entrySet()
                             .stream()
                             .map(entity -> CoinAvailability.of(entity.getKey(), entity.getValue()))
                             .collect(Collectors.toList());
    }
}
