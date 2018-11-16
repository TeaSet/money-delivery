package com.revolut.money.delivery

import com.revolut.money.delivery.datastore.DataStoreImpl
import com.revolut.money.delivery.model.AccountId
import com.revolut.money.delivery.model.Money
import com.revolut.money.delivery.service.impl.AccountServiceImpl
import com.revolut.money.delivery.service.impl.AccountSynchronizerImpl
import com.revolut.money.delivery.service.impl.TransactionServiceImpl
import spock.lang.Shared
import spock.lang.Specification

class TransactionServiceImplTest extends Specification {

    @Shared
    def transactionService = new TransactionServiceImpl()

    @Shared
    def accountService = new AccountServiceImpl()

    @Shared
    def accountSynchronizer = new AccountSynchronizerImpl()

    def setupSpec() {
        accountService.setDataStore(new DataStoreImpl())
        transactionService.setAccountService(accountService)
        transactionService.setAccountSynchronizer(accountSynchronizer)
    }

    def "get balance of created account"() {
        setup:
        Money money = new Money(100.0, "USD")
        AccountId accountId = accountService.createAccount("My_Name", money)
        when:
        Money currBalance = transactionService.getCurrentBalance(accountId)
        then:
        currBalance == money
        currBalance.amount == money.amount
        currBalance.currencyCode == money.currencyCode
    }

    def "deposit some money to account"() {
        setup:
        Money money = new Money(100.0, "USD")
        AccountId accountId = accountService.createAccount("My_Name", money)
        when:
        transactionService.deposit(accountId, new Money(50, "USD"))
        Money newBalance = transactionService.getCurrentBalance(accountId)
        then:
        newBalance.amount == money.amount + 50.0
        newBalance.currencyCode == money.currencyCode
    }

    def "deposit negative amount of money"() {
        setup:
        Money money = new Money(100.0, "USD")
        AccountId accountId = accountService.createAccount("My_Name", money)
        when:
        transactionService.deposit(accountId, new Money(-100.0, "USD"))
        then:
        RuntimeException ex = thrown()
        ex.message == "You try to deposit wrong format of money"
    }

    def "deposit money to locked account"() {
        setup:
        Money money = new Money(100.0, "USD")
        AccountId accountId = accountService.createAccount("My_Name", money)
        accountService.lockAccount(accountId)
        when:
        transactionService.deposit(accountId, money)
        then:
        RuntimeException ex = thrown()
        ex.message == "Your account is locked"
    }

    def "withdraw some money from account"() {
        setup:
        Money money = new Money(100.0, "USD")
        AccountId accountId = accountService.createAccount("My_Name", money)
        when:
        transactionService.withdraw(accountId, new Money(50, "USD"))
        Money newBalance = transactionService.getCurrentBalance(accountId)
        then:
        newBalance.amount == money.amount - 50.0
        newBalance.currencyCode == money.currencyCode
    }

    def "withdraw negative amount of money"() {
        setup:
        Money money = new Money(100.0, "USD")
        AccountId accountId = accountService.createAccount("My_Name", money)
        when:
        transactionService.withdraw(accountId, new Money(-100, "USD"))
        then:
        RuntimeException ex = thrown()
        ex.message == "You try to withdraw wrong format of money"
    }

    def "withdraw money from locked account"() {
        setup:
        Money money = new Money(100.0, "USD")
        AccountId accountId = accountService.createAccount("My_Name", money)
        accountService.lockAccount(accountId)
        when:
        transactionService.withdraw(accountId, new Money(100, "USD"))
        then:
        RuntimeException ex = thrown()
        ex.message == "Your account is locked"
    }

    def "transfer money from one to another"() {
        setup:
        Money senderMoney = new Money(250.0, "USD")
        AccountId fromAccount = accountService.createAccount("Sender", senderMoney)

        Money receiverMoney = new Money(100.0, "USD")
        AccountId toAccount = accountService.createAccount("Receiver", receiverMoney)
        when:
        transactionService.transfer(fromAccount, toAccount, new Money(50.0, "USD"))
        Money senderNewBalance = transactionService.getCurrentBalance(fromAccount)
        Money receiverNewBalance = transactionService.getCurrentBalance(toAccount)
        then:
        senderNewBalance.amount == senderMoney.amount - 50.0
        receiverNewBalance.amount == receiverMoney.amount + 50.0
    }

    def "transfer negative amount of money"() {
        setup:
        Money senderMoney = new Money(250.0, "USD")
        AccountId fromAccount = accountService.createAccount("Sender", senderMoney)

        Money receiverMoney = new Money(100.0, "USD")
        AccountId toAccount = accountService.createAccount("Receiver", receiverMoney)
        when:
        transactionService.transfer(fromAccount, toAccount, new Money(-50.0, "USD"))
        then:
        RuntimeException ex = thrown()
        ex.message == "You try to transfer wrong format of money"
    }

    def "transfer money to locked account"() {
        setup:
        Money senderMoney = new Money(250.0, "USD")
        AccountId fromAccount = accountService.createAccount("Sender", senderMoney)

        Money receiverMoney = new Money(100.0, "USD")
        AccountId toAccount = accountService.createAccount("Receiver", receiverMoney)
        accountService.lockAccount(toAccount)
        when:
        transactionService.transfer(fromAccount, toAccount, new Money(50.0, "USD"))
        then:
        RuntimeException ex = thrown()
        ex.message == "Your account is locked"
    }

}
