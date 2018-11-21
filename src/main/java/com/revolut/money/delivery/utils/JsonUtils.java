package com.revolut.money.delivery.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

public class JsonUtils {

    public static BigDecimal getBigDecimalValue(Object object) {
        Objects.requireNonNull(object, "Value of the object from Json must be not null");

        BigDecimal result;
        if (object instanceof BigDecimal)
            result = (BigDecimal) object;
        else if (object instanceof String)
            result = new BigDecimal((String) object);
        else if (object instanceof BigInteger)
            result = new BigDecimal((BigInteger) object);
        else if (object instanceof Number)
            result = new BigDecimal(String.valueOf(object));
        else
            throw new ClassCastException("Could not get value of the current object");

        result = result.setScale(4, RoundingMode.HALF_UP);
        return result;
    }
}
