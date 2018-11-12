package com.revolut.money.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private Long accountId;

    private BigDecimal balance;

    private String currencyCode;
}
