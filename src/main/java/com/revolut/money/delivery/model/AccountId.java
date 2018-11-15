package com.revolut.money.delivery.model;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class AccountId {

    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    private String accountHolder;
    private int accountNum;

    public AccountId(String accountHolder) {
        this.accountHolder = accountHolder;
        this.accountNum = COUNTER.getAndIncrement();
    }

    public AccountId(String accountHolder, int accountNum) {
        this.accountHolder = accountHolder;
        this.accountNum = accountNum;
    }

    @Override
    public String toString() {
        return "AccountId{" +
                "accountHolder='" + accountHolder + '\'' +
                ", accountNum='" + accountNum + '\'' +
                '}';
    }
}
