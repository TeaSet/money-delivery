package com.revolut.money.delivery.datastore;

import com.revolut.money.delivery.datastore.api.DataStore;
import com.revolut.money.delivery.model.Account;
import com.revolut.money.delivery.model.AccountId;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStoreImpl implements DataStore {

    private static Map<AccountId, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void store(Account account) {
        accounts.putIfAbsent(account.getAccountId(), account);
    }

    @Override
    public Account getAccount(AccountId accountId) {
        return accounts.get(accountId);
    }

    @Override
    public void remove(AccountId accountId) {
        accounts.remove(accountId);
    }

    @Override
    public void cleanup() {
        accounts.clear();
    }
}
