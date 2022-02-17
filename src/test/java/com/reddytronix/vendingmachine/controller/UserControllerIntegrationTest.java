package com.reddytronix.vendingmachine.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.reddytronix.vendingmachine.model.User;
import com.reddytronix.vendingmachine.model.UserRole;
import com.reddytronix.vendingmachine.repository.UserRepository;
import com.reddytronix.vendingmachine.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

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
        authenticatedRestTemplate = restTemplate.withBasicAuth(BUYER_USER_NAME, BUYER_USER_PASSWORD);
    }

    @Test
    void veryBuyerUserRegistration() {

        //given
        final UserRepresentation buyerUser = new UserRepresentation();
        buyerUser.setUsername(BUYER_USER_NAME);
        buyerUser.setPassword(BUYER_USER_PASSWORD);
        buyerUser.setRole(UserRole.BUYER);

        //when
        final ResponseEntity<UserRepresentation> response = restTemplate.postForEntity(getUserResourceUrl(), buyerUser,
                                                                                       UserRepresentation.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        final UserRepresentation responseUser = response.getBody();
        assertThat(responseUser.getUsername()).isEqualTo("buyerUser");
        assertThat(responseUser.getPassword()).isNull();
        assertThat(responseUser.getDeposit()).isEqualTo(0);
        assertThat(responseUser.getRole()).isEqualTo(UserRole.BUYER);
    }

    @Test
    void shouldVerifyGetUser() {
        //given
        final User buyerUser = new User();
        buyerUser.setUsername(BUYER_USER_NAME);
        buyerUser.setPassword("buyerUserPass");
        buyerUser.setRole(UserRole.BUYER);
        userService.createUser(buyerUser);

        //when
        final ResponseEntity<UserRepresentation> response = authenticatedRestTemplate
                                                                .getForEntity(
                                                                    getUserResourceUrl() + "/" + BUYER_USER_NAME,
                                                                    UserRepresentation.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final UserRepresentation responseUser = response.getBody();
        assertThat(responseUser.getUsername()).isEqualTo("buyerUser");
        assertThat(responseUser.getPassword()).isNull();
        assertThat(responseUser.getDeposit()).isEqualTo(0);
        assertThat(responseUser.getRole()).isEqualTo(UserRole.BUYER);
    }

    @Test
    void shouldVerifyDeleteUser() {
        //given
        final User buyerUser = new User();
        buyerUser.setUsername(BUYER_USER_NAME);
        buyerUser.setPassword(BUYER_USER_PASSWORD);
        buyerUser.setRole(UserRole.BUYER);
        userService.createUser(buyerUser);

        //when
        authenticatedRestTemplate.delete(getUserResourceUrl() + "/" + BUYER_USER_NAME);

        //then
        final long count = userRepository.count();
        assertThat(count).isEqualTo(0);
    }

    private String getUserResourceUrl() {
        return "http://localhost:" + port + "/user";
    }
}