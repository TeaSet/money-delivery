package com.revolut.money.delivery.service.impl;

import com.revolut.money.delivery.model.Account;
import com.revolut.money.delivery.model.Money;
import com.revolut.money.delivery.service.api.AccountService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.stream.Collectors;

public class AccountServiceImpl extends AbstractVerticle implements AccountService {

    private JDBCClient jdbcClient;

    @Override
    public void start(Future<Void> future) {
        jdbcClient = JDBCClient.createShared(vertx, new JsonObject()
                .put("url", "jdbc:hsqldb:file:db/accounts")
                .put("http.port", "8082")
                .put("driver_class", "org.hsqldb.jdbcDriver"), "Accounts");

        startBackend(
                (connection) -> createTable(connection,
                        (nothing) -> startWebApp(
                                (http) -> completeStartup(http, future)
                        ), future
                ), future);
    }

    private void startBackend(Handler<AsyncResult<SQLConnection>> next, Future<Void> future) {
        jdbcClient.getConnection(asyncResult -> {
            if (asyncResult.failed()) {
                future.fail(asyncResult.cause());
            } else {
                next.handle(Future.succeededFuture(asyncResult.result()));
            }
        });
    }

    private void startWebApp(Handler<AsyncResult<HttpServer>> next) {
        Router router = Router.router(vertx);

        router.route("/").handler(context -> {
            HttpServerResponse response = context.response();
            response.putHeader("content-type", "text/html").end();
        });
        router.get("/account").handler(this::getAllAccounts);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080, next::handle);
    }

    private void completeStartup(AsyncResult<HttpServer> server, Future<Void> future) {
        if (server.succeeded()) {
            future.complete();
        } else {
            future.fail(server.cause());
        }
    }

    private void createTable(AsyncResult<SQLConnection> result, Handler<AsyncResult<Void>> next, Future<Void> future) {
        if (result.failed()) {
            future.fail(result.cause());
        } else {
            SQLConnection connection = result.result();
            connection.execute(
                    "CREATE TABLE IF NOT EXISTS Account (accountId INTEGER IDENTITY, amount DECIMAL, currency VARCHAR(100), isLocked BOOLEAN)",
                    asyncResult -> {
                        if (asyncResult.failed()) {
                            future.fail(asyncResult.cause());
                            connection.close();
                            return;
                        }
                    }
            );
        }
    }

    @Override
    public void stop() {
        jdbcClient.close();
    }

    @Override
    public Account createAccount() {
        return null;
    }

    @Override
    public Account getAccountById(Long accountId) {
        return null;
    }

    @Override
    public void getAllAccounts(RoutingContext context) {
        jdbcClient.getConnection(asyncResult -> {
            SQLConnection connection = asyncResult.result();
            connection.query("SELECT * FROM Account", result -> {
                List<Account> whiskies = result.result().getRows().stream().map(Account::new).collect(Collectors.toList());
                context.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(whiskies));
                connection.close();
            });
        });
    }

    @Override
    public void removeAccount(Long accountId) {

    }

    @Override
    public Money getCurrentBalance(Long accountId) {
        return null;
    }

    @Override
    public void transferFund(Long accountId, Money money) {

    }

    @Override
    public void withdrawFund(Long accountId, Money money) {

    }

    @Override
    public void depositFund(Long accountId, Money money) {

    }
}
