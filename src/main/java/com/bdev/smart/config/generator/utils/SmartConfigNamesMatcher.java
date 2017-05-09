package com.bdev.smart.config.generator.utils;

public class SmartConfigNamesMatcher {
    public static String getPropertyAccessorName(String propertyName) {
        return "get" +
                propertyName.substring(0, 1).toUpperCase() +
                propertyName.substring(1);
    }
}
