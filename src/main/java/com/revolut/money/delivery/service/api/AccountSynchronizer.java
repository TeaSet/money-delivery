package com.revolut.money.delivery.service.api;

import com.revolut.money.delivery.model.AccountId;

/**
 * This interface synchronizes accounts during bank operations
 */
public interface AccountSynchronizer {

    void lockCurrentAccount(AccountId accountId);

    void unlockCurrentAccount(AccountId accountId);

    void lockAction(AccountId accountId1, Runnable runnable);

    void lockAction(AccountId accountId1, AccountId accountId2, Runnable runnable);
}
