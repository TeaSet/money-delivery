package com.revolut.money.delivery;

import com.revolut.money.delivery.service.impl.AccountServiceImpl;

public class Launcher {

    public static void main(String[] args) {
        System.out.println("Vert.x application starting...");
        if (args.length > 0) {
            io.vertx.core.Launcher.main(args);
        } else {
            io.vertx.core.Launcher.main(new String[]{"run", AccountServiceImpl.class.getName()});
        }
    }
}
