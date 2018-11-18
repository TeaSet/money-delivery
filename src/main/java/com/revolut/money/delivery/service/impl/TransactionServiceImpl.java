package com.revolut.money.delivery.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revolut.money.delivery.model.Account;
import com.revolut.money.delivery.model.AccountId;
import com.revolut.money.delivery.model.Money;
import com.revolut.money.delivery.service.api.AccountService;
import com.revolut.money.delivery.service.api.AccountSynchronizer;
import com.revolut.money.delivery.service.api.ExchangeService;
import com.revolut.money.delivery.service.api.TransactionService;

@Singleton
public class TransactionServiceImpl implements TransactionService {

    private AccountSynchronizer accountSynchronizer;
    private AccountService accountService;
    private ExchangeService exchangeService;

    @Inject
    public void setAccountSynchronizer(AccountSynchronizer accountSynchronizer) {
        this.accountSynchronizer = accountSynchronizer;
    }

    @Inject
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Inject
    public void setExchangeService(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @Override
    public Money getCurrentBalance(AccountId id) {
        Account account = accountService.getAccount(id);
        return account.getMoney();
    }

    @Override
    public void deposit(AccountId id, Money moneyToPut) {
        if (moneyToPut.getAmount().signum() < 0) {
            throw new RuntimeException("You try to deposit wrong format of money");
        } else {
            Account account = accountService.getAccount(id);
            accountSynchronizer.lockAction(id, () -> {
                checkLock(id);
                Money money = getCurrentBalance(id);
                Money afterConvert = exchangeService.exchange(moneyToPut,
                                                              moneyToPut.getCurrencyCode(),
                                                              money.getCurrencyCode());
                Money newBalance = new Money(money.getAmount().add(afterConvert.getAmount()), money.getCurrencyCode());
                account.setMoney(newBalance);
            });
        }
    }

    @Override
    public void withdraw(AccountId id, Money moneyToFetch) {
        if (moneyToFetch.getAmount().signum() < 0) {
            throw new RuntimeException("You try to withdraw wrong format of money");
        } else {
            Account account = accountService.getAccount(id);
            accountSynchronizer.lockAction(id, () -> {
                checkLock(id);
                Money money = getCurrentBalance(id);
                Money afterConvert = exchangeService.exchange(moneyToFetch,
                                                              moneyToFetch.getCurrencyCode(),
                                                              money.getCurrencyCode());
                if (money.getAmount().compareTo(afterConvert.getAmount()) < 0) {
                    throw new RuntimeException("Your balance is not enough to withdraw " + afterConvert.getAmount());
                }
                Money newBalance = new Money(money.getAmount().subtract(afterConvert.getAmount()), money.getCurrencyCode());
                account.setMoney(newBalance);
            });
        }
    }

    @Override
    public void transfer(AccountId fromId, AccountId toId, Money moneyToSend) {
        if (moneyToSend.getAmount().signum() < 0) {
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
        Account account = accountService.getAccount(accountId);
        if (account.isLocked()) {
            throw new RuntimeException("Your account is locked");
        }
    }
}
