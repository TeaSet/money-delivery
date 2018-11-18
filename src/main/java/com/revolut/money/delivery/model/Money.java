package com.revolut.money.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@ToString
public class Money {

    private BigDecimal amount;
    private String currencyCode;

    public Money() {
        this.amount = new BigDecimal("0.0");
        this.currencyCode = "USD";
    }
}
