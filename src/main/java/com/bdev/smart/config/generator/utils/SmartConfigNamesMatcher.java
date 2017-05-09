package com.bdev.smart.config.generator.utils;

import com.bdev.smart.config.data.util.Tuple;

import java.util.List;

public class SmartConfigNamesMatcher {
    public static String getDimensionPropertiesInstanceName(
            List<Tuple<String, String>> dimensionValues
    ) {
        String dimensionPropertiesClassName = getDimensionPropertiesClassName(dimensionValues);

        return
                dimensionPropertiesClassName.substring(0, 1).toLowerCase() +
                dimensionPropertiesClassName.substring(1);
    }

    public static String getDimensionPropertiesClassName(
            List<Tuple<String, String>> dimensionValues
    ) {
        StringBuilder sb = new StringBuilder();

        for (Tuple<String, String> dimensionValue : dimensionValues) {
            sb.append(getFirstUppercaseDimensionValueName(dimensionValue.getB()));
        }

        sb.append("SmartConfig");

        return sb.toString();
    }

    private static String getFirstUppercaseDimensionValueName(String dimensionValue) {
        return
                dimensionValue.substring(0, 1).toUpperCase() +
                dimensionValue.substring(1);
    }

    public static String getPropertyAccessorName(String propertyName) {
        return "get" +
                propertyName.substring(0, 1).toUpperCase() +
                propertyName.substring(1);
    }
}
