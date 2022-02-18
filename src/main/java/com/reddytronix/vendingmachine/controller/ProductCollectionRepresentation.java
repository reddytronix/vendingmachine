package com.reddytronix.vendingmachine.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ProductCollectionRepresentation {

    private List<ProductRepresentation> products;
}
