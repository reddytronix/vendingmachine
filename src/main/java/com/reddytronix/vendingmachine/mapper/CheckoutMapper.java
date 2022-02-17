package com.reddytronix.vendingmachine.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.reddytronix.vendingmachine.controller.CheckoutRepresentation;
import com.reddytronix.vendingmachine.model.Checkout;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface CheckoutMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target="productId", source="productId")
    @Mapping(target="quantity", source="quantity")
    Checkout fromCheckoutRepresentation(CheckoutRepresentation checkoutRepresentation);

    CheckoutRepresentation toCheckoutRepresentation(Checkout checkout);
}
