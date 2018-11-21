package com.revolut.money.delivery.controller.schemas;

public enum JsonSchemas {

    NEW_ACCOUNT_SCHEMA("{\n" +
            "  \"type\": \"object\",\n" +
            "  \"properties\": {\n" +
            "    \"accountHolder\": {\n" +
            "      \"type\": \"string\"\n" +
            "    },\n" +
            "    \"amount\": {\n" +
            "      \"type\": \"number\"\n" +
            "    },\n" +
            "    \"currency\": {\n" +
            "      \"type\": \"string\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"additionalProperties\": false,\n" +
            "  \"required\": [\n" +
            "    \"accountHolder\",\n" +
            "    \"amount\",\n" +
            "    \"currency\"\n" +
            "  ]\n" +
            "}"),
    DEPOSIT_AND_WITHDRAW_SCHEMA("{\n" +
            "  \"type\": \"object\",\n" +
            "  \"properties\": {\n" +
            "    \"accountHolder\": {\n" +
            "      \"type\": \"string\"\n" +
            "    },\n" +
            "    \"accountNum\": {\n" +
            "      \"type\": \"integer\"\n" +
            "    },\n" +
            "    \"amount\": {\n" +
            "      \"type\": \"number\"\n" +
            "    },\n" +
            "    \"currency\": {\n" +
            "      \"type\": \"string\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"additionalProperties\": false,\n" +
            "  \"required\": [\n" +
            "    \"accountHolder\",\n" +
            "    \"accountNum\",\n" +
            "    \"amount\",\n" +
            "    \"currency\"\n" +
            "  ]\n" +
            "}"),
    TRANSFER_SCHEMA("{\n" +
            "  \"type\": \"object\",\n" +
            "  \"properties\": {\n" +
            "    \"fromAccountHolder\": {\n" +
            "      \"type\": \"string\"\n" +
            "    },\n" +
            "    \"fromAccountNum\": {\n" +
            "      \"type\": \"integer\"\n" +
            "    },\n" +
            "    \"toAccountHolder\": {\n" +
            "      \"type\": \"string\"\n" +
            "    },\n" +
            "    \"toAccountNum\": {\n" +
            "      \"type\": \"integer\"\n" +
            "    },\n" +
            "    \"amount\": {\n" +
            "      \"type\": \"number\"\n" +
            "    },\n" +
            "    \"currency\": {\n" +
            "      \"type\": \"string\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"additionalProperties\": false,\n" +
            "  \"required\": [\n" +
            "    \"fromAccountHolder\",\n" +
            "    \"fromAccountNum\",\n" +
            "    \"toAccountHolder\",\n" +
            "    \"toAccountNum\",\n" +
            "    \"amount\",\n" +
            "    \"currency\"\n" +
            "  ]\n" +
            "}");

    private final String schema;

    JsonSchemas(String schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return schema;
    }
}
