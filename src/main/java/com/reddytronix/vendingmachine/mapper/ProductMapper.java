package com.reddytronix.vendingmachine.mapper;

import org.mapstruct.Mapper;

import com.reddytronix.vendingmachine.controller.ProductRepresentation;
import com.reddytronix.vendingmachine.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product fromProductRepresentation(ProductRepresentation productRepresentation);

    ProductRepresentation toProductRepresentation(Product product);
}
