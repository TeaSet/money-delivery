package com.revolut.money.delivery

import com.revolut.money.delivery.datastore.DataStoreImpl
import com.revolut.money.delivery.model.Account
import com.revolut.money.delivery.model.AccountId
import com.revolut.money.delivery.model.Money
import com.revolut.money.delivery.service.impl.AccountServiceImpl
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

class AccountServiceImplTest extends Specification {

    @Shared
    def accountService = new AccountServiceImpl()

    @Shared
    def money = new Money(100.5, "USD")

    def setupSpec() {
        accountService.setDataStore(new DataStoreImpl())
    }

    @Ignore
    def "create new account"() {
        when:
        def accountId = accountService.createAccount("My_Name", money)
        def account = new Account(accountId, money)
        then:
        account.accountId != null
        account.accountId.accountHolder == "My_Name"
        account.accountId.accountNum == 1
        !account.locked
        account.money.amount == money.amount
    }

    @Ignore
    def "get existing account"() {
        setup:
        def currentAccount = new Account()
        currentAccount.with {
            accountId = new AccountId("My_Name", 1)
            locked = false
            money = new Money()
            money.with {
                amount = 100.5
                currencyCode = "USD"
            }
        }
        expect:
        currentAccount == accountService.getAccount(new AccountId("My_Name", 1))
    }

    def "create, get and delete existing account"() {
        def accountId = accountService.createAccount("My_Name", money)
        def createdAccount = new Account(accountId, money)
        def id = accountId
        expect:
        createdAccount == accountService.getAccount(id)
        accountService.removeAccount(id)
        when:
        accountService.getAccount(id)
        then:
        noExceptionThrown()
    }

    def "lock-unlock and get account"() {
        when:
        def accountId = accountService.createAccount("My_Name", money)
        def createdAccount = new Account(accountId, money)
        def id = accountId
        then:
        !createdAccount.locked
        when:
        accountService.lockAccount(id)
        createdAccount = accountService.getAccount(id)
        then:
        createdAccount.locked
        when:
        accountService.unlockAccount(id)
        createdAccount = accountService.getAccount(id)
        then:
        !createdAccount.locked
    }
}
