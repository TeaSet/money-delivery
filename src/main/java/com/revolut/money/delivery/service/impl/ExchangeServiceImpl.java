package com.revolut.money.delivery.service.impl;

import com.google.inject.Singleton;
import com.revolut.money.delivery.model.Money;
import com.revolut.money.delivery.utils.Pair;
import com.revolut.money.delivery.service.api.ExchangeService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ExchangeServiceImpl implements ExchangeService {

    private static final Map<Pair<String, String>, BigDecimal> exchangeRates =
            Collections.unmodifiableMap(new HashMap<Pair<String, String>, BigDecimal>() {{
                put(new Pair<>("RUB", "RUB"), BigDecimal.valueOf(1D));
                put(new Pair<>("USD", "USD"), BigDecimal.valueOf(1D));
                put(new Pair<>("EUR", "EUR"), BigDecimal.valueOf(1D));
                put(new Pair<>("RUB", "USD"), BigDecimal.valueOf(0.015));
                put(new Pair<>("RUB", "EUR"), BigDecimal.valueOf(0.013));
                put(new Pair<>("USD", "RUB"), BigDecimal.valueOf(65.99));
                put(new Pair<>("USD", "EUR"), BigDecimal.valueOf(0.875));
                put(new Pair<>("EUR", "RUB"), BigDecimal.valueOf(75.35));
                put(new Pair<>("EUR", "USD"), BigDecimal.valueOf(1.142));
            }});
    @Override
    public Money exchange(Money amount, String currentCurrency, String targetCurrency) {
        return new Money(amount.getAmount().multiply(exchangeRates.get(new Pair<>(currentCurrency, targetCurrency))),
                targetCurrency);
    }
}
