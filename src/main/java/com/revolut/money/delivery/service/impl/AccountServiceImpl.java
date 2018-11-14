package com.revolut.money.delivery.service.impl;

import com.revolut.money.delivery.datastore.DataStore;
import com.revolut.money.delivery.model.Account;
import com.revolut.money.delivery.model.AccountId;
import com.revolut.money.delivery.model.Money;
import com.revolut.money.delivery.service.api.AccountService;


public class AccountServiceImpl implements AccountService {

    private DataStore dataStore;

    public AccountServiceImpl() {
        this.dataStore = new DataStore();
    }

    @Override
    public Account getAccount(AccountId accountId) {
        return dataStore.getAccount(accountId);
    }

    @Override
    public AccountId createAccount(String accountHolder, Money money) {
        Account account = new Account(accountHolder, money.getAmount(), money.getCurrencyCode());

        dataStore.store(account);

        return account.getAccountId();
    }

    @Override
    public void removeAccount(AccountId accountId) {
        dataStore.remove(accountId);
    }

    @Override
    public void lockAccount(AccountId accountId) {
        Account account = getAccount(accountId);
        account.setLocked(true);
    }

    @Override
    public void unlockAccount(AccountId accountId) {
        Account account = getAccount(accountId);
        account.setLocked(false);
    }
}