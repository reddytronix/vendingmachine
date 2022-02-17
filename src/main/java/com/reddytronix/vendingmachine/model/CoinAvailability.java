package com.reddytronix.vendingmachine.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class CoinAvailability {
    private final String coinType;
    private final int quantity;

    public static CoinAvailability of(final Integer coinValue, final Integer quantity) {
        return new CoinAvailability(coinValue + " cent", quantity);
    }
}
