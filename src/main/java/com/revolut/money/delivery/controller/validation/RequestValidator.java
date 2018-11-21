package com.revolut.money.delivery.controller.validation;

import com.revolut.money.delivery.controller.schemas.JsonSchemas;
import io.vertx.ext.web.api.validation.HTTPRequestValidationHandler;
import io.vertx.ext.web.api.validation.ParameterType;

public class RequestValidator {

    public static HTTPRequestValidationHandler createNewAccountValidator() {
        return HTTPRequestValidationHandler.create().addJsonBodySchema(JsonSchemas.NEW_ACCOUNT_SCHEMA.toString());
    }

    public static HTTPRequestValidationHandler depositWithdrawValidator() {
        return HTTPRequestValidationHandler.create().addJsonBodySchema(JsonSchemas.DEPOSIT_AND_WITHDRAW_SCHEMA.toString());
    }

    public static HTTPRequestValidationHandler transferValidator() {
        return HTTPRequestValidationHandler.create().addJsonBodySchema(JsonSchemas.TRANSFER_SCHEMA.toString());
    }

    public static HTTPRequestValidationHandler getDeleteAccountValidator() {
        return HTTPRequestValidationHandler.create()
                .addQueryParam("accountHolder", ParameterType.GENERIC_STRING, true)
                .addQueryParam("accountNum", ParameterType.INT, true);
    }
}
