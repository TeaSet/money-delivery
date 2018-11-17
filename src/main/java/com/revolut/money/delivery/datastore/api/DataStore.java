package com.revolut.money.delivery.datastore.api;

import com.revolut.money.delivery.model.Account;
import com.revolut.money.delivery.model.AccountId;

public interface DataStore {

    void store(Account account);

    Account getAccount(AccountId accountId);

    void remove(AccountId accountId);
}
