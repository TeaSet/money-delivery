package com.revolut.money.delivery.service.impl;

import com.google.inject.Inject;
import com.revolut.money.delivery.datastore.api.DataStore;
import com.revolut.money.delivery.model.Account;
import com.revolut.money.delivery.model.AccountId;
import com.revolut.money.delivery.model.Money;
import com.revolut.money.delivery.service.api.AccountService;
import com.revolut.money.delivery.service.api.AccountSynchronizer;
import com.revolut.money.delivery.service.api.TransactionService;

public class TransactionServiceImpl implements TransactionService {

    private DataStore dataStore;
    private AccountSynchronizer accountSynchronizer;
    private AccountService accountService;

    @Inject
    public void setAccountSynchronizer(AccountSynchronizer accountSynchronizer) {
        this.accountSynchronizer = accountSynchronizer;
    }

    @Inject
    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Inject
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Money getCurrentBalance(AccountId id) {
        Account account = dataStore.getAccount(id);
        return account.getMoney();
    }

    @Override
    public void deposit(AccountId id, Money moneyToPut) {
        if (moneyToPut.getAmount() < 0) {
            throw new RuntimeException("You try to deposit wrong format of money");
        } else {
            Account account = dataStore.getAccount(id);
            accountSynchronizer.lockAction(id, () -> {
                checkLock(id);
                Money money = getCurrentBalance(id);
                Money newBalance = new Money(money.getAmount() + moneyToPut.getAmount(), money.getCurrencyCode());
                account.setMoney(newBalance);
            });
        }
    }

    @Override
    public void withdraw(AccountId id, Money moneyToFetch) {
        if (moneyToFetch.getAmount() < 0) {
            throw new RuntimeException("You try to withdraw wrong format of money");
        } else {
            Account account = dataStore.getAccount(id);
            accountSynchronizer.lockAction(id, () -> {
                checkLock(id);
                Money money = getCurrentBalance(id);
                Money newBalance = new Money(money.getAmount() - moneyToFetch.getAmount(), money.getCurrencyCode());
                account.setMoney(newBalance);
            });
        }
    }

    @Override
    public void transfer(AccountId fromId, AccountId toId, Money moneyToSend) {
        if (moneyToSend.getAmount() < 0) {
            throw new RuntimeException("You try to transfer wrong format of money");
        } else {
            accountSynchronizer.lockAction(fromId, toId, () -> {
                checkLock(fromId);
                checkLock(toId);
                withdraw(fromId, moneyToSend);
                deposit(toId, moneyToSend);
            });
        }
    }

    private void checkLock(AccountId accountId) {
        Account account = dataStore.getAccount(accountId);
        if (account.isLocked()) {
            throw new RuntimeException("Your account is locked");
        }
    }
}
