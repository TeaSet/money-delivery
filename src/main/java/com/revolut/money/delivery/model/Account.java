package com.revolut.money.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private Long accountId;

    private Money balance;

    private boolean isLocked;
}
