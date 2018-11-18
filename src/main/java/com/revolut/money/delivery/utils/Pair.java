package com.revolut.money.delivery.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<A, B> {
    private A fromCurrentCurrency;
    private B toTargetCurrency;
}
