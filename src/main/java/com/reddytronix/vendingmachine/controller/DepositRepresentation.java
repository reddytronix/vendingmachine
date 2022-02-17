package com.reddytronix.vendingmachine.controller;

import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class DepositRepresentation {

    //Request Attribute
    @Min(5)
    private Integer coinValue;

    // Response Attribute
    private Long depositAmount;
}
