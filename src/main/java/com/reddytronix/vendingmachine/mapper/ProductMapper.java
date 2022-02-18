package com.reddytronix.vendingmachine.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.reddytronix.vendingmachine.controller.ProductRepresentation;
import com.reddytronix.vendingmachine.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product fromProductRepresentation(ProductRepresentation productRepresentation);

    ProductRepresentation toProductRepresentation(Product product);

    List<ProductRepresentation> toProductRepresentations(List<Product> products);
}
