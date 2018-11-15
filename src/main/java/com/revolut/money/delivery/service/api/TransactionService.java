package com.revolut.money.delivery.service.api;

import com.revolut.money.delivery.model.AccountId;
import com.revolut.money.delivery.model.Money;

public interface TransactionService {

    Money getCurrentBalance(AccountId id);

    void deposit(AccountId id, Money moneyToPut);

    void withdraw(AccountId id, Money moneyToFetch);

    void transfer(AccountId fromId, AccountId toId, Money moneyToSend);
}
