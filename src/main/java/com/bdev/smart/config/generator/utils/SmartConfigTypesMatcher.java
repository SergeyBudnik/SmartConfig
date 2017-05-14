package com.bdev.smart.config.generator.utils;

import com.bdev.smart.config.data.inner.property.PropertyType;

public class SmartConfigTypesMatcher {
    public static String getType(PropertyType propertyType) {
        return "SmartConfigValue<" + getUnboxedType(propertyType) + ">";
    }

    private static String getUnboxedType(PropertyType propertyType) {
        switch (propertyType) {
            case NUMBER:
                return "Long";
            case BOOLEAN:
                return "Boolean";
            case STRING:
                return "String";
            case LIST_OF_NUMBERS:
                return "List<Long>";
            case LIST_OF_BOOLEANS:
                return "List<Boolean>";
            case LIST_OF_STRINGS:
                return "List<String>";
        }

        throw new RuntimeException();
    }
}
