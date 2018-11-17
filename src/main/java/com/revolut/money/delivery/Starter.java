package com.revolut.money.delivery;

import com.revolut.money.delivery.controller.AccountManagementController;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class Starter extends AbstractVerticle {

    private AccountManagementController accountMgmtController;

    public Starter() {
        this.accountMgmtController = new AccountManagementController();
    }

    public Router getRoutes() {
        Router router = Router.router(vertx);
        router.route("/accounts/*").handler(BodyHandler.create());

        router.get("/accounts").handler(accountMgmtController::getAccount);
        router.post("/accounts").handler(accountMgmtController::createAccount);
        router.delete("/accounts").handler(accountMgmtController::removeAccount);
        router.post("/accounts/lock").handler(accountMgmtController::lockAccount);
        router.post("/accounts/unlock").handler(accountMgmtController::unlockAccount);
        router.post("/accounts/deposit").handler(accountMgmtController::deposit);
        router.post("/accounts/withdraw").handler(accountMgmtController::withdraw);
        router.post("/accounts/transfer").handler(accountMgmtController::transfer);

        return router;
    }

    @Override
    public void start(Future<Void> fut) {

        Router router = getRoutes();

        vertx.createHttpServer().requestHandler(router::accept)
                                .listen(config().getInteger("http.port", 8080),
                                    result -> {
                                        if (result.succeeded()) {
                                            fut.complete();
                                         } else {
                                            fut.fail(result.cause());
                                         }
                                    }
                                );
    }

    public static void main(String[] args) {
        System.out.println("Vert.x application starting...");
        if (args.length > 0) {
            io.vertx.core.Launcher.main(args);
        } else {
            io.vertx.core.Launcher.main(new String[]{"run", Starter.class.getName()});
        }
    }
}
