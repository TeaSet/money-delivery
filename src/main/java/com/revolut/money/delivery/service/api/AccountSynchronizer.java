package com.revolut.money.delivery.service.api;

/**
 * This interface synchronizes accounts during bank operations
 */
public interface AccountSynchronizer {

    void lockCurrentAccount(Long accountId);

    void unlockCurrentAccount(Long accountId);

    void lockAction(Long accountId1, Runnable runnable);

    void lockAction(Long accountId1, Long accountId2, Runnable runnable);
}
