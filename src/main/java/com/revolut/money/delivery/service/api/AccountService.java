package com.revolut.money.delivery.service.api;

import com.revolut.money.delivery.model.Account;
import com.revolut.money.delivery.model.AccountId;
import com.revolut.money.delivery.model.Money;

public interface AccountService {

    AccountId createAccount(String accountHolder, Money money);

    Account getAccount(AccountId accountId);

    void removeAccount(AccountId accountId);

    void lockAccount(AccountId accountId);

    void unlockAccount(AccountId accountId);

}
