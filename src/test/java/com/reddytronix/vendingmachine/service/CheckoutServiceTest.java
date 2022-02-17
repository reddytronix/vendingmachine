package com.reddytronix.vendingmachine.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.reddytronix.vendingmachine.model.Checkout;
import com.reddytronix.vendingmachine.model.CoinAvailability;
import com.reddytronix.vendingmachine.model.Product;
import com.reddytronix.vendingmachine.model.User;
import com.reddytronix.vendingmachine.security.AuthService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CheckoutService checkoutService;

    @Test
    void verifyCheckout() {
        final User buyer = new User();
        buyer.setDeposit(180);

        final User seller = new User();
        seller.setDeposit(15);

        final Product product = new Product();
        product.setId(11L);
        product.setSellerId(101L);
        product.setAmountAvailable(15);
        product.setCost(5);

        Checkout request = new Checkout();
        request.setProductId(11L);
        request.setQuantity(7L);

        when(authService.getCurrentAuthenticatedUsername()).thenReturn("buyerUsername");
        when(userService.findUserByUsername("buyerUsername")).thenReturn(buyer);

        when(userService.findUserById(101L)).thenReturn(seller);
        when(productService.findProductById(11L)).thenReturn(product);

        final Checkout response = checkoutService.checkout(request);

        assertEquals(response.getTotalOrderAmount(), 35);
        assertEquals(response.getRemainingDepositAmount(), 145);
        assertEquals(buyer.getDeposit(), 145);
        assertEquals(seller.getDeposit(), 50);
        assertEquals(product.getAmountAvailable(), 8);

        assertThat(response.getAvailableCoins()).hasSize(5)
                                                .contains(CoinAvailability.of(5, 1))
                                                .contains(CoinAvailability.of(10, 0))
                                                .contains(CoinAvailability.of(20, 2))
                                                .contains(CoinAvailability.of(50, 0))
                                                .contains(CoinAvailability.of(100, 1));
        verify(userService).saveUser(buyer);
        verify(userService).saveUser(seller);
        verify(productService).saveProduct(product);

    }
}