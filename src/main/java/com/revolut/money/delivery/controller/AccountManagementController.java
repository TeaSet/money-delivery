package com.revolut.money.delivery.controller;

import com.google.inject.Guice;
import com.revolut.money.delivery.di.ApplicationInjectorModule;
import com.revolut.money.delivery.model.Account;
import com.revolut.money.delivery.model.AccountId;
import com.revolut.money.delivery.model.Money;
import com.revolut.money.delivery.service.api.AccountService;
import com.revolut.money.delivery.service.api.TransactionService;
import com.revolut.money.delivery.utils.JsonUtils;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.math.BigDecimal;


public class AccountManagementController {

    private final AccountService accountService;

    private final TransactionService transactionService;

    public AccountManagementController() {
        accountService = Guice.createInjector(new ApplicationInjectorModule()).getInstance(AccountService.class);
        transactionService = Guice.createInjector(new ApplicationInjectorModule()).getInstance(TransactionService.class);
    }

    public void getAccount(RoutingContext context) {
        String accountHolder = context.request().getParam("accountHolder");
        int accountNum = Integer.valueOf(context.request().getParam("accountNum"));

        AccountId accountId = new AccountId(accountHolder, accountNum);

        Account account = accountService.getAccount(accountId);
        if(account == null) {
            response(context, 500,
                    Json.encodePrettily("The Account requested does not exist"));
        }

        response(context, 200, Json.encodePrettily(account));
    }

    public void createAccount(RoutingContext context) {
        JsonObject bodyAsJson = context.getBodyAsJson();
        String actHolder = bodyAsJson.getString("holder");
        BigDecimal amount = JsonUtils.getBigDecimalValue(bodyAsJson.getValue("amount"));
        String currencyCode = bodyAsJson.getString("currencyCode");

        Money money = new Money(amount, currencyCode);


        if (actHolder == null || currencyCode == null) {
            response(context, 400,
                    Json.encodePrettily("Please provide account holder name and currency code"));
        }
        AccountId accountCreated = accountService.createAccount(actHolder, money);
        response(context, 201, Json.encodePrettily(accountCreated));
    }

    public void removeAccount(RoutingContext context) {
        String accountHolder = context.request().getParam("accountHolder");
        int accountNum = Integer.valueOf(context.request().getParam("accountNum"));

        AccountId accountId = new AccountId(accountHolder, accountNum);
        accountService.removeAccount(accountId);
        response(context, 200, Json.encodePrettily(accountId));

    }

    public void lockAccount(RoutingContext context) {
        String accountHolder = context.request().getParam("accountHolder");
        int accountNum = Integer.valueOf(context.request().getParam("accountNum"));

        AccountId accountId = new AccountId(accountHolder, accountNum);
        accountService.lockAccount(accountId);
        response(context, 200, Json.encodePrettily(accountId));
    }

    public void unlockAccount(RoutingContext context) {
        String accountHolder = context.request().getParam("accountHolder");
        int accountNum = Integer.valueOf(context.request().getParam("accountNum"));

        AccountId accountId = new AccountId(accountHolder, accountNum);
        accountService.unlockAccount(accountId);
        response(context, 200, Json.encodePrettily(accountId));
    }

    public void deposit(RoutingContext context) {
        JsonObject bodyAsJson = context.getBodyAsJson();
        String accountHolder = bodyAsJson.getString("accountHolder");
        int accountNum = bodyAsJson.getInteger("accountNum");
        AccountId accountId = new AccountId(accountHolder, accountNum);
        BigDecimal amount = JsonUtils.getBigDecimalValue(bodyAsJson.getValue("amount"));
        String currency = bodyAsJson.getString("currency");
        Money moneyToPut = new Money(amount, currency);
        transactionService.deposit(accountId, moneyToPut);
        response(context, 202, Json.encodePrettily(moneyToPut));
    }

    public void withdraw(RoutingContext context) {
        JsonObject bodyAsJson = context.getBodyAsJson();
        String accountHolder = bodyAsJson.getString("accountHolder");
        int accountNum = bodyAsJson.getInteger("accountNum");
        AccountId accountId = new AccountId(accountHolder, accountNum);
        BigDecimal amount = JsonUtils.getBigDecimalValue(bodyAsJson.getValue("amount"));
        String currency = bodyAsJson.getString("currency");
        Money moneyToFetch = new Money(amount, currency);
        transactionService.withdraw(accountId, moneyToFetch);
        response(context, 202, Json.encodePrettily(moneyToFetch));
    }

    public void transfer(RoutingContext context) {
        JsonObject bodyAsJson = context.getBodyAsJson();

        String fromAccountHolder = bodyAsJson.getString("fromAccountHolder");
        int fromAccountNum = bodyAsJson.getInteger("fromAccountNum");
        AccountId fromAccountId = new AccountId(fromAccountHolder, fromAccountNum);

        String toAccountHolder = bodyAsJson.getString("toAccountHolder");
        int toAccountNum = bodyAsJson.getInteger("toAccountNum");
        AccountId toAccountId = new AccountId(toAccountHolder, toAccountNum);

        BigDecimal amount = JsonUtils.getBigDecimalValue(bodyAsJson.getValue("amount"));
        String currency = bodyAsJson.getString("currency");
        Money moneyToTransfer = new Money(amount, currency);

        transactionService.transfer(fromAccountId, toAccountId, moneyToTransfer);
        response(context, 202, Json.encodePrettily(moneyToTransfer));
    }

    private void response(RoutingContext routingContext, int httpCode, String body) {
        routingContext.response()
                .setStatusCode(httpCode)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(body);
    }
}
