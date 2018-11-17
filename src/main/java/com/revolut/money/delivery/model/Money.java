package com.revolut.money.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Money {

    private double amount;
    private String currencyCode;

    public Money() {
        this.amount = 0.0;
        this.currencyCode = "USD";
    }
}
