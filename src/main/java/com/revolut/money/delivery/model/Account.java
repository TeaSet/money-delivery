package com.revolut.money.delivery.model;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private Long accountId;

    private Money balance;

    private boolean isLocked;

    public Account(JsonObject jsonObject) {
        this.accountId = jsonObject.getLong("accountId");
        this.balance = new Money(new BigDecimal(jsonObject.getString("amount")), jsonObject.getString("currency"));
        this.isLocked = jsonObject.getBoolean("isLocked");
    }
}
