package com.revolut.money.delivery.service.api;

import com.revolut.money.delivery.model.Account;
import com.revolut.money.delivery.model.Money;

import java.util.List;

public interface AccountService {

    Account createAccount();

    Account getAccountById(Long accountId);

    List<Account> getAllAccounts();

    void removeAccount(Long accountId);

    Money getCurrentBalance(Long accountId);

    void transferFund(Long accountId, Money money);

    void withdrawFund(Long accountId, Money money);

    void depositFund(Long accountId, Money money);



}
