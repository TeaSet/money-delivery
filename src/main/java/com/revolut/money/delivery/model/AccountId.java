package com.revolut.money.delivery.model;

import lombok.Data;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@ToString
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
}
