package com.revolut.money.delivery.service.api;

import com.revolut.money.delivery.model.Money;

public interface ExchangeService {

    Money exchange (Money amount, String currentCurrency, String targetCurrency);
}
