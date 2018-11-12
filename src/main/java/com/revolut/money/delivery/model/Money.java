package com.revolut.money.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Money {

    private BigDecimal amount;

    private String currencyCode;
}
