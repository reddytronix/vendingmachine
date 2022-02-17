package com.reddytronix.vendingmachine.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.reddytronix.vendingmachine.mapper.CheckoutMapper;
import com.reddytronix.vendingmachine.model.Checkout;
import com.reddytronix.vendingmachine.service.CheckoutService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final CheckoutMapper checkoutMapper;

    @PreAuthorize("hasAuthority('BUYER')")
    @PostMapping("/buy")
    public ResponseEntity<CheckoutRepresentation> buy(@RequestBody final CheckoutRepresentation checkoutRepresentation) {
        final Checkout checkout = checkoutMapper.fromCheckoutRepresentation(checkoutRepresentation);
        final Checkout responseCheckout = checkoutService.checkout(checkout);
        return ResponseEntity.ok(checkoutMapper.toCheckoutRepresentation(responseCheckout));
    }
}
