package com.reddytronix.vendingmachine.controller;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ProductRepresentation {

    private Long id;

    @NotBlank
    private String productName;

    @Min(1)
    private long amountAvailable;

    @Min(5)
    private long cost;

    private Long sellerId;
}
