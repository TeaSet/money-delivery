package com.revolut.money.delivery

import com.revolut.money.delivery.model.AccountId
import com.revolut.money.delivery.service.impl.AccountSynchronizerImpl
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.Executors

class AccountSynchronizerImplTest extends Specification {
    def synchronizer = new AccountSynchronizerImpl()
    @Shared
    def executors = Executors.newFixedThreadPool(4)

    def "test account locking"() {
        setup:
        def accountId = new AccountId("My_Name", 100)
        when:
        synchronizer.lockCurrentAccount(accountId)
        then:
        synchronizer.lockMap.containsKey(accountId)
    }

    def "test account unlocking"() {
        setup:
        def accountId = new AccountId("His_Name", 200)
        synchronizer.lockCurrentAccount(accountId)
        when:
        synchronizer.unlockCurrentAccount(accountId)
        then:
        !synchronizer.lockMap.containsKey(accountId)
    }

    def "test when two account don't block"() {
        setup:
        def id1 = new AccountId("My_Name")
        def id2 = new AccountId("His_Name")
        synchronizer.lockCurrentAccount(id1)
        synchronizer.lockCurrentAccount(id2)
        def count1 = 0, count2 = 0, tasks = []
        when:
        1000.times {
            tasks << {synchronizer.lockAction(id1, id2) {count1++}}
            tasks << {synchronizer.lockAction(id2, id1) {count2++}}
        }
        executors.invokeAll(tasks).forEach({it.get()})
        then:
        count1 == 1000
        count2 == 1000
    }

    def "test for one and two accounts that do not block each other"() {
        setup:
        def id1 = new AccountId("My_Name")
        def id2 = new AccountId("His_Name")
        synchronizer.lockCurrentAccount(id1)
        synchronizer.lockCurrentAccount(id2)
        def count1 = 0, count2 = 0, count3 = 0, tasks = []
        when:
        1000.times {
            tasks << {synchronizer.lockAction(id1) {count1++}}
            tasks << {synchronizer.lockAction(id2) {count2++}}
            tasks << {synchronizer.lockAction(id1, id2) {count3++}}
        }
        executors.invokeAll(tasks).forEach({it.get()})
        then:
        count1 == 1000
        count2 == 1000
        count3 == 1000
    }

    def "test associative of lockAction method"() {
        setup:
        def id1 = new AccountId("My_Name")
        def id2 = new AccountId("His_Name")
        def id3 = new AccountId("Her_Name")
        synchronizer.lockCurrentAccount(id1)
        synchronizer.lockCurrentAccount(id2)
        synchronizer.lockCurrentAccount(id3)
        def counter = 100, tasks = []
        when:
        1000.times {
            tasks << {synchronizer.lockAction(id1, id2) {counter++}}
            tasks << {synchronizer.lockAction(id2, id3) {counter--}}
        }
        executors.invokeAll(tasks).forEach({it.get()})
        then:
        counter == 100
    }

    def cleanupSpec() {
        if (!executors.isTerminated())
            executors.shutdownNow()
    }
}
