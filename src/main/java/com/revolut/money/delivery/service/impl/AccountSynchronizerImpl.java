package com.revolut.money.delivery.service.impl;

import com.google.inject.Singleton;
import com.revolut.money.delivery.model.AccountId;
import com.revolut.money.delivery.service.api.AccountSynchronizer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Singleton
public class AccountSynchronizerImpl implements AccountSynchronizer {

    Map<AccountId, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    @Override
    public void lockCurrentAccount(AccountId accountId) {
        lockMap.putIfAbsent(accountId, new ReentrantLock());
    }

    @Override
    public void unlockCurrentAccount(AccountId accountId) {
        lockMap.remove(accountId);
    }

    @Override
    public void lockAction(AccountId account1, Runnable runnable) {
        lockCurrentAccount(account1);
        ReentrantLock lock = lockMap.get(account1);
        lock.lock();
        try {
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void lockAction(AccountId accountId1, AccountId accountId2, Runnable runnable) {
        boolean isLocked = false;
        lockCurrentAccount(accountId1);
        ReentrantLock lock1 = lockMap.get(accountId1);

        lockCurrentAccount(accountId2);
        ReentrantLock lock2 = lockMap.get(accountId2);
        do {
            if (lock1.tryLock())
                if (lock2.tryLock())
                    isLocked = true;
                else
                    lock1.unlock();
        } while (!isLocked);
        try {
            runnable.run();
        } finally {
            lock2.unlock();
            lock1.unlock();
        }
    }
}
