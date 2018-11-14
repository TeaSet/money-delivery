package com.revolut.money.delivery.datastore;

import com.revolut.money.delivery.model.Account;
import com.revolut.money.delivery.model.AccountId;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    private static Map<AccountId, Account> accounts = new ConcurrentHashMap<>();

    public void store(Account account) {
        accounts.putIfAbsent(account.getAccountId(), account);
    }

    public Account getAccount(AccountId accountId) {
        return accounts.get(accountId);
    }

    public void remove(AccountId accountId) {
        accounts.remove(accountId);
    }
}
