package com.revolut.money.delivery.service.api;

import com.revolut.money.delivery.model.Account;

public interface AccountService {

    Account createAccount();
    void removeAccount(Long accountId);
}
