package com.revolut.money.delivery.di;

import com.google.inject.AbstractModule;
import com.revolut.money.delivery.datastore.DataStoreImpl;
import com.revolut.money.delivery.datastore.api.DataStore;
import com.revolut.money.delivery.service.api.AccountService;
import com.revolut.money.delivery.service.api.AccountSynchronizer;
import com.revolut.money.delivery.service.api.TransactionService;
import com.revolut.money.delivery.service.impl.AccountServiceImpl;
import com.revolut.money.delivery.service.impl.AccountSynchronizerImpl;
import com.revolut.money.delivery.service.impl.TransactionServiceImpl;

public class ApplicationInjectorModule extends AbstractModule {

    @Override
    public void configure() {
        bind(AccountService.class).to(AccountServiceImpl.class);
        bind(AccountSynchronizer.class).to(AccountSynchronizerImpl.class);
        bind(DataStore.class).to(DataStoreImpl.class);
        bind(TransactionService.class).to(TransactionServiceImpl.class);
    }
}
