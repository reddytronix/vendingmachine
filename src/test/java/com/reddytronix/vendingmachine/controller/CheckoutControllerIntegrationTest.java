package com.reddytronix.vendingmachine.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import com.reddytronix.vendingmachine.model.CoinAvailability;
import com.reddytronix.vendingmachine.model.User;
import com.reddytronix.vendingmachine.model.UserRole;
import com.reddytronix.vendingmachine.repository.UserRepository;
import com.reddytronix.vendingmachine.service.ProductService;
import com.reddytronix.vendingmachine.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CheckoutControllerIntegrationTest {

    private static final String BUYER_USER_NAME = "buyerUser";
    private static final String BUYER_USER_PASSWORD = "buyerUserPass";
    private static final String SELLER_USER_NAME = "sellerUser";
    private static final String SELLER_USER_PASSWORD = "sellerUserPass";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    private TestRestTemplate buyerRestTemplate;
    private TestRestTemplate sellerRestTemplate;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        buyerRestTemplate = restTemplate.withBasicAuth(BUYER_USER_NAME, BUYER_USER_PASSWORD);
        sellerRestTemplate = restTemplate.withBasicAuth(SELLER_USER_NAME, SELLER_USER_PASSWORD);

        final User buyerUser = new User();
        buyerUser.setUsername(BUYER_USER_NAME);
        buyerUser.setPassword(BUYER_USER_PASSWORD);
        buyerUser.setRole(UserRole.BUYER);
        buyerUser.setDeposit(150);
        userService.createUser(buyerUser);

        final User sellerUser = new User();
        sellerUser.setUsername(SELLER_USER_NAME);
        sellerUser.setPassword(SELLER_USER_PASSWORD);
        sellerUser.setRole(UserRole.SELLER);
        userService.createUser(sellerUser);
    }

    @Test
    void shouldVerifyGetUser() {
        //given
        final ProductRepresentation productRepresentation = new ProductRepresentation();
        productRepresentation.setProductName("Orange Juice");
        productRepresentation.setCost(5);
        productRepresentation.setAmountAvailable(25);

        final ResponseEntity<ProductRepresentation> productResponse = sellerRestTemplate
                                                                          .postForEntity(
                                                                              getProductResourceUrl(),
                                                                              productRepresentation,
                                                                              ProductRepresentation.class);
        final ProductRepresentation product = productResponse.getBody();

        final CheckoutRepresentation checkout = new CheckoutRepresentation();
        checkout.setProductId(product.getId());
        checkout.setQuantity(8L);

        //when
        final ResponseEntity<CheckoutRepresentation> checkoutResponse = buyerRestTemplate
                                                                            .postForEntity(
                                                                                getBuyResourceUrl(), checkout,
                                                                                CheckoutRepresentation.class);
        //then
        final CheckoutRepresentation checkoutRepresentation = checkoutResponse.getBody();

        assertThat(checkoutRepresentation.getRemainingDepositAmount()).isEqualTo(110);
        assertThat(checkoutRepresentation.getProduct()).isNotNull();
        assertThat(checkoutRepresentation.getProduct().getId()).isEqualTo(product.getId());
        assertThat(checkoutRepresentation.getTotalOrderAmount()).isEqualTo(40);

        assertThat(checkoutRepresentation.getAvailableCoins()).hasSize(5)
                                                              .contains(CoinAvailability.of(5, 0))
                                                              .contains(CoinAvailability.of(10, 1))
                                                              .contains(CoinAvailability.of(20, 0))
                                                              .contains(CoinAvailability.of(50, 0))
                                                              .contains(CoinAvailability.of(100, 1));

    }

    private String getBuyResourceUrl() {
        return "http://localhost:" + port + "/buy";
    }

    private String getProductResourceUrl() {
        return "http://localhost:" + port + "/products";
    }
}