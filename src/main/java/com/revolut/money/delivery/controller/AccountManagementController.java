package com.revolut.money.delivery.controller;

import com.google.inject.Guice;
import com.revolut.money.delivery.di.ApplicationInjectorModule;
import com.revolut.money.delivery.model.Account;
import com.revolut.money.delivery.model.AccountId;
import com.revolut.money.delivery.model.Money;
import com.revolut.money.delivery.service.api.AccountService;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;


public class AccountManagementController {

    private final AccountService accountService;

    public AccountManagementController() {
        accountService = Guice.createInjector(new ApplicationInjectorModule()).getInstance(AccountService.class);
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
        double amount = bodyAsJson.getDouble("amount");
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

    private void response(RoutingContext routingContext, int httpCode, String body) {
        routingContext.response()
                .setStatusCode(httpCode)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(body);
    }
}
