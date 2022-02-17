package com.reddytronix.vendingmachine.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.reddytronix.vendingmachine.model.User;
import com.reddytronix.vendingmachine.model.UserRole;
import com.reddytronix.vendingmachine.repository.UserRepository;
import com.reddytronix.vendingmachine.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DepositControllerTest {

    private static final String BUYER_USER_NAME = "buyerUser";
    private static final String BUYER_USER_PASSWORD = "buyerUserPass";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private TestRestTemplate authenticatedRestTemplate;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        final User buyerUser = new User();
        buyerUser.setUsername(BUYER_USER_NAME);
        buyerUser.setPassword(BUYER_USER_PASSWORD);
        buyerUser.setRole(UserRole.BUYER);
        userService.createUser(buyerUser);
        authenticatedRestTemplate = restTemplate.withBasicAuth(BUYER_USER_NAME, BUYER_USER_PASSWORD);
    }

    @Test
    void verifyDeposit() {

        //given
        final DepositRepresentation depositRepresentation = new DepositRepresentation();
        depositRepresentation.setCoinValue(50);

        //when Deposit Coin
        ResponseEntity<DepositRepresentation> response =
            authenticatedRestTemplate
                .postForEntity(getDepositResourceUrl(), depositRepresentation, DepositRepresentation.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DepositRepresentation responseBody = response.getBody();
        assertThat(responseBody.getCoinValue()).isNull();
        assertThat(responseBody.getDepositAmount()).isEqualTo(50);

        ResponseEntity<UserRepresentation> userResponse =
            authenticatedRestTemplate
                .getForEntity(getUserResourceUrl() + "/" + BUYER_USER_NAME, UserRepresentation.class);
        assertThat(userResponse.getBody().getDeposit()).isEqualTo(50);

        //given
        depositRepresentation.setCoinValue(10);
        //when
        response =
            authenticatedRestTemplate
                .postForEntity(getDepositResourceUrl(), depositRepresentation, DepositRepresentation.class);
        responseBody = response.getBody();
        assertThat(responseBody.getDepositAmount()).isEqualTo(60);

        //then
        userResponse =
            authenticatedRestTemplate
                .getForEntity(getUserResourceUrl() + "/" + BUYER_USER_NAME, UserRepresentation.class);

        assertThat(userResponse.getBody().getDeposit()).isEqualTo(60);

    }

    @Test
    void verifyReset() {

        //given
        final DepositRepresentation depositRepresentation = new DepositRepresentation();
        depositRepresentation.setCoinValue(50);

        ResponseEntity<DepositRepresentation> response =
            authenticatedRestTemplate
                .postForEntity(getDepositResourceUrl(), depositRepresentation, DepositRepresentation.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DepositRepresentation responseBody = response.getBody();
        assertThat(responseBody.getCoinValue()).isNull();
        assertThat(responseBody.getDepositAmount()).isEqualTo(50);

        //when Reset
        authenticatedRestTemplate.delete(getResetResourceUrl());

        //then
        ResponseEntity<UserRepresentation> userResponse =
            authenticatedRestTemplate
                .getForEntity(getUserResourceUrl() + "/" + BUYER_USER_NAME, UserRepresentation.class);

        assertThat(userResponse.getBody().getDeposit()).isEqualTo(0);
    }

    private String getDepositResourceUrl() {
        return "http://localhost:" + port + "/deposit";
    }

    private String getResetResourceUrl() {
        return "http://localhost:" + port + "/reset";
    }

    private String getUserResourceUrl() {
        return "http://localhost:" + port + "/user";
    }
}