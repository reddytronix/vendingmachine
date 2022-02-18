package com.reddytronix.vendingmachine.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.reddytronix.vendingmachine.exception.ProductNotFoundException;
import com.reddytronix.vendingmachine.exception.UserUnauthorisedException;
import com.reddytronix.vendingmachine.model.Product;
import com.reddytronix.vendingmachine.model.User;
import com.reddytronix.vendingmachine.repository.ProductRepository;
import com.reddytronix.vendingmachine.security.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final AuthService authService;
    private final ProductRepository productRepository;

    public Product createProduct(final Product newProduct) {
        final User currentAuthenticatedUser = authService.getCurrentAuthenticatedUser();
        newProduct.setSellerId(currentAuthenticatedUser.getId());
        return productRepository.save(newProduct);
    }

    public Product findProductById(final Long productId) {
        return productRepository.findById(productId)
                                .orElseThrow(
                                    () -> new ProductNotFoundException(
                                        "Product (Id: " + productId + ") does not exists"));
    }

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public Product updateProduct(final Product product) {

        final Product productToBeUpdated = findProductById(product.getId());

        verifyAuthenticateUserIsAuthorisedToUpdateProduct(productToBeUpdated);

        productToBeUpdated.setProductName(product.getProductName());
        productToBeUpdated.setAmountAvailable(product.getAmountAvailable());
        productToBeUpdated.setCost(product.getCost());

        return productRepository.save(productToBeUpdated);
    }

    public void saveProduct(final Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(final Long productId) {
        final Product productToBeDeleted = findProductById(productId);

        verifyAuthenticateUserIsAuthorisedToUpdateProduct(productToBeDeleted);
        productRepository.delete(productToBeDeleted);
    }

    private void verifyAuthenticateUserIsAuthorisedToUpdateProduct(Product product) {
        final User currentAuthenticatedUser = authService.getCurrentAuthenticatedUser();
        if (!Objects.equals(product.getSellerId(), currentAuthenticatedUser.getId())) {
            throw new UserUnauthorisedException(
                "User is not authorised to modify product, Id : " + product.getId());
        }
    }
}
