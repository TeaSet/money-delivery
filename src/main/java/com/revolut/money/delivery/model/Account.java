package com.revolut.money.delivery.model;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Account {

    private AccountId accountId;

    private Money money;

    private boolean isLocked;

    public Account(AccountId accountId, Money money) {
        this.accountId = accountId;
        this.money = money;
        this.isLocked = false;
    }

    public Account(String accountHolder, double amount, String currencyCode) {
        this.accountId = new AccountId(accountHolder);
        this.money = new Money(amount, currencyCode);
        this.isLocked = false;
    }
}
