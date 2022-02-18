package com.reddytronix.vendingmachine.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.reddytronix.vendingmachine.mapper.ProductMapper;
import com.reddytronix.vendingmachine.model.Product;
import com.reddytronix.vendingmachine.service.ProductService;
import com.reddytronix.vendingmachine.validation.ProductValidator;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ProductValidator productValidator;

    @PreAuthorize("hasAuthority('SELLER')")
    @PostMapping("/products")
    public ResponseEntity<ProductRepresentation> createProduct(@RequestBody @Valid final ProductRepresentation productRepresentation) {

        productValidator.validate(productRepresentation);

        final Product product = productMapper.fromProductRepresentation(productRepresentation);
        final Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toProductRepresentation(createdProduct));
    }

    @GetMapping("/products")
    public ResponseEntity<ProductCollectionRepresentation> getProducts() {
        final List<Product> products = productService.getProducts();
        final List<ProductRepresentation> productRepresentations = productMapper.toProductRepresentations(products);
        final ProductCollectionRepresentation collectionRepresentation = new ProductCollectionRepresentation();
        collectionRepresentation.setProducts(productRepresentations);
        return ResponseEntity.ok(collectionRepresentation);
    }

    @PreAuthorize("hasAuthority('SELLER')")
    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductRepresentation> updateProduct(@PathVariable final Long productId,
                                                               @RequestBody final ProductRepresentation productRepresentation) {

        productValidator.validate(productRepresentation);

        final Product product = productMapper.fromProductRepresentation(productRepresentation);
        product.setId(productId);

        final Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity.ok(productMapper.toProductRepresentation(updatedProduct));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductRepresentation> getProduct(@PathVariable Long productId) {
        final Product product = productService.findProductById(productId);
        return ResponseEntity.ok(productMapper.toProductRepresentation(product));
    }

    @PreAuthorize("hasAuthority('SELLER')")
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ProductRepresentation> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
