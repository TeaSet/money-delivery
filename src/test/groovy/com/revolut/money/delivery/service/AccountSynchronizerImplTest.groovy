package com.revolut.money.delivery.service

import com.revolut.money.delivery.model.AccountId
import com.revolut.money.delivery.service.impl.AccountSynchronizerImpl
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Timeout

import java.util.concurrent.ExecutionException
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

    @Timeout(10)
    def "exception does not block account"() {
        setup:
        def id1 = new AccountId("My_Name")
        synchronizer.lockCurrentAccount(id1)
        when:
        executors.submit {synchronizer.lockAction(id1) {throw new RuntimeException("This is Execution exception")}}.get()
        then:
        def exception = thrown(ExecutionException.class)
        exception.getCause().class == RuntimeException.class
        when:
        executors.submit {synchronizer.lockAction(id1) {println("This is ok!")}}.get()
        then:
        noExceptionThrown()
    }

    @Timeout(10)
    def "test when two account don't block each other"() {
        setup:
        def id1 = new AccountId("My_Name", 3)
        def id2 = new AccountId("His_Name", 1)
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

    @Timeout(10)
    def "test when two account don't block each other when exception"() {
        setup:
        def id1 = new AccountId("My_Name")
        def id2 = new AccountId("His_Name")
        synchronizer.lockCurrentAccount(id1)
        synchronizer.lockCurrentAccount(id2)
        def count1 = 0, count2 = 0, count3 = 0, tasks = []
        when:
        1000.times {i ->
            tasks << {synchronizer.lockAction(id1) {
                if (i % 2 == 1)
                    count1++
                else throw new RuntimeException("some exception of id1")
            }}
            tasks << {synchronizer.lockAction(id2) {
                if (i % 2 == 0)
                    count2++
                else throw new RuntimeException("some exception of id2")
            }}
            tasks << {synchronizer.lockAction(id1, id2) {count3++}}
        }
        executors.invokeAll(tasks).forEach({
            try {
                it.get()
            } catch (Exception e) {}
        })
        then:
        count1 == 500
        count2 == 500
        count3 == 1000
    }

    @Timeout(10)
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

    @Timeout(10)
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
