package com.bdev.smart.config.generator.utils;

import com.bdev.smart.config.data.inner.property.PropertyType;

public class SmartConfigTypesMatcher {
    public static String getConfigType(PropertyType propertyType) {
        return "SmartConfigValue<" + getType(propertyType, false) + ">";
    }

    public static String getType(PropertyType propertyType, boolean forcePrimitive) {
        switch (propertyType) {
            case NUMBER:
                return forcePrimitive ? "long" : "Long";
            case BOOLEAN:
                return forcePrimitive ? "boolean" : "Boolean";
            case STRING:
                return "String";
            case LIST_OF_NUMBERS:
                return "List<Long>";
            case LIST_OF_BOOLEANS:
                return "List<Boolean>";
            case LIST_OF_STRINGS:
                return "List<String>";
        }

        throw new RuntimeException("Unexpected property type: '" + propertyType + "'");
    }
}
