package com.reddytronix.vendingmachine.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.reddytronix.vendingmachine.exception.InsufficientDepositException;
import com.reddytronix.vendingmachine.exception.LowProductQuantityException;
import com.reddytronix.vendingmachine.model.Checkout;
import com.reddytronix.vendingmachine.model.CoinAvailability;
import com.reddytronix.vendingmachine.model.Product;
import com.reddytronix.vendingmachine.model.User;
import com.reddytronix.vendingmachine.security.AuthService;

import lombok.RequiredArgsConstructor;

import static com.reddytronix.vendingmachine.utils.CoinUtils.getCoinAvailability;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckoutService {

    private final AuthService authService;
    private final UserService userService;
    private final ProductService productService;

    public Checkout checkout(final Checkout checkout) {

        final String buyerUsername = authService.getCurrentAuthenticatedUsername();
        final User buyer = userService.findUserByUsername(buyerUsername);

        final Product product = productService.findProductById(checkout.getProductId());
        verifyInventoryAvailability(checkout, product);

        final long totalProductCost = checkout.getQuantity() * product.getCost();
        verifyBuyerHasSufficientDeposit(buyer, totalProductCost);

        final User seller = userService.findUserById(product.getSellerId());

        final long remainingDepositAmount = buyer.getDeposit() - totalProductCost;
        final long remainingQuantity = product.getAmountAvailable() - checkout.getQuantity();
        final long sellerDepositAmount = seller.getDeposit() + totalProductCost;

        buyer.setDeposit(remainingDepositAmount);
        seller.setDeposit(sellerDepositAmount);
        product.setAmountAvailable(remainingQuantity);

        final List<CoinAvailability> coinAvailabilities = getCoinAvailability(remainingDepositAmount);

        userService.saveUser(buyer);
        userService.saveUser(seller);
        productService.saveProduct(product);

        final Checkout responseCheckout = new Checkout();
        responseCheckout.setRemainingDepositAmount(remainingDepositAmount);
        responseCheckout.setProduct(product);
        responseCheckout.setTotalOrderAmount(totalProductCost);
        responseCheckout.setAvailableCoins(coinAvailabilities);

        return responseCheckout;

    }

    private void verifyInventoryAvailability(final Checkout checkout, final Product product) {
        if (checkout.getQuantity() > product.getAmountAvailable()) {
            throw new LowProductQuantityException("Requested product quantity " + checkout.getQuantity() + " not "
                                                  + "available. Available quantity is " + product.getAmountAvailable());
        }
    }

    private void verifyBuyerHasSufficientDeposit(final User buyer, final long totalCost) {
        if (buyer.getDeposit() < totalCost) {
            throw new InsufficientDepositException("Deposit amount " + buyer.getDeposit() + " is not sufficient to buy "
                                                   + "product. Total cost is " + totalCost);
        }
    }
}
