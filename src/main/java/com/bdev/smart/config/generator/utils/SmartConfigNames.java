package com.bdev.smart.config.generator.utils;

public class SmartConfigNames {
    public static String getPropertyConfigAccessorName(String propertyName) {
        return "get" +
                propertyName.substring(0, 1).toUpperCase() +
                propertyName.substring(1) +
                "Config";
    }

    public static String getPropertyConfigSetterName(String propertyName) {
        return "set" +
                propertyName.substring(0, 1).toUpperCase() +
                propertyName.substring(1) +
                "Config";
    }

    public static String getPropertyAccessorName(String propertyName) {
        return "get" +
                propertyName.substring(0, 1).toUpperCase() +
                propertyName.substring(1);
    }
}
