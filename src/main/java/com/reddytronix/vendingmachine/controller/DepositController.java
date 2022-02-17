package com.reddytronix.vendingmachine.controller;

import java.util.Arrays;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.reddytronix.vendingmachine.exception.InvalidDepositException;
import com.reddytronix.vendingmachine.service.UserService;

import lombok.RequiredArgsConstructor;

import static com.reddytronix.vendingmachine.utils.CoinUtils.SUPPORTED_COINS;

@RestController
@RequiredArgsConstructor
public class DepositController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('BUYER')")
    @PostMapping("/deposit")
    public ResponseEntity<DepositRepresentation> deposit(
        @RequestBody @Valid final DepositRepresentation depositRepresentation) {

        if (depositRepresentation.getCoinValue() == null ||
            Arrays.stream(SUPPORTED_COINS).noneMatch(value -> value == depositRepresentation.getCoinValue())) {
            throw new InvalidDepositException("Deposit supports only 5, 10, 20, 50 and 100 cents coin. Invalid coin : "
                                              + depositRepresentation.getCoinValue());
        }

        final long depositAmount = userService.deposit(depositRepresentation.getCoinValue());

        final DepositRepresentation response = new DepositRepresentation();
        response.setDepositAmount(depositAmount);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('BUYER')")
    @DeleteMapping("/reset")
    public ResponseEntity<DepositRepresentation> deposit() {

        userService.reset();

        return ResponseEntity.noContent().build();
    }

}
