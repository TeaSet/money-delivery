package com.revolut.money.delivery.service

import com.revolut.money.delivery.model.Money
import com.revolut.money.delivery.service.impl.ExchangeServiceImpl
import spock.lang.Shared
import spock.lang.Specification

class ExchangeServiceImplTest extends Specification {

    @Shared
    def exchangeService = new ExchangeServiceImpl()

    Money money = new Money(100.0, "USD")

    def "exchange from USD to RUB"() {
        when:
        Money newBalance = exchangeService.exchange(money, "USD", "RUB")
        then:
        newBalance.amount == BigDecimal.valueOf(6599)
        newBalance.currencyCode == "RUB"
    }

    def "exchange from USD to unsupported rate"() {
        when:
        exchangeService.exchange(money, "USD", "BIT")
        then:
        thrown(RuntimeException)
    }

    def "exchange from USD to USD"() {
        when:
        Money newBalance = exchangeService.exchange(money, "USD", "USD")
        then:
        newBalance.amount == BigDecimal.valueOf(100)
        newBalance.currencyCode == "USD"
    }
}
