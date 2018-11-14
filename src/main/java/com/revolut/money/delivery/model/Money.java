package com.revolut.money.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Money {

    private double amount;
    private String currencyCode;

    public Money() {
        this.amount = 0.0;
        this.currencyCode = "USD";
    }
}
