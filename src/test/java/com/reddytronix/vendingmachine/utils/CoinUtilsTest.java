package com.reddytronix.vendingmachine.utils;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.reddytronix.vendingmachine.model.CoinAvailability;

import static org.assertj.core.api.Assertions.assertThat;

class CoinUtilsTest {

    @Test
    void verifyCoinAvailabilityFor180() {
        final List<CoinAvailability> coinAvailability = CoinUtils.getCoinAvailability(180);

        assertThat(coinAvailability).hasSize(5)
                                    .contains(CoinAvailability.of(5, 0))
                                    .contains(CoinAvailability.of(10, 1))
                                    .contains(CoinAvailability.of(20, 1))
                                    .contains(CoinAvailability.of(50, 1))
                                    .contains(CoinAvailability.of(100, 1));

    }

    @Test
    void verifyCoinAvailabilityFor50() {
        final List<CoinAvailability> coinAvailability = CoinUtils.getCoinAvailability(50);

        assertThat(coinAvailability).hasSize(5)
                                    .contains(CoinAvailability.of(5, 0))
                                    .contains(CoinAvailability.of(10, 0))
                                    .contains(CoinAvailability.of(20, 0))
                                    .contains(CoinAvailability.of(50, 1))
                                    .contains(CoinAvailability.of(100, 0));

    }

    @Test
    void verifyCoinAvailabilityFor75() {
        final List<CoinAvailability> coinAvailability = CoinUtils.getCoinAvailability(75);

        assertThat(coinAvailability).hasSize(5)
                                    .contains(CoinAvailability.of(5, 1))
                                    .contains(CoinAvailability.of(10, 0))
                                    .contains(CoinAvailability.of(20, 1))
                                    .contains(CoinAvailability.of(50, 1))
                                    .contains(CoinAvailability.of(100, 0));

    }
}