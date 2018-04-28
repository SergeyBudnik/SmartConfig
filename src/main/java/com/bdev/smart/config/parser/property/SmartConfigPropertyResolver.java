package com.bdev.smart.config.parser.property;

import java.util.ArrayList;
import java.util.List;

class SmartConfigPropertyResolver {
    static String getName(String propertyKey) {
        StringBuilder stringBuilder = new StringBuilder();

        String [] propertyKeyParts = propertyKey.replace("\"", "").split("[-.]");

        for (String propertyKeyPart : propertyKeyParts) {
            if (propertyKeyPart.startsWith("~")) {
                break;
            }

            if (stringBuilder.length() == 0) {
                stringBuilder.append(getLowerCaseStartingNamePart(propertyKeyPart));
            } else {
                stringBuilder.append(getUpperCaseStartingNamePart(propertyKeyPart));
            }
        }

        return stringBuilder.toString();
    }

    static List<String> getDimensions(String propertyKey) {
        String [] propertyKeyParts = propertyKey.replace("\"", "").split("[-.]");

        for (String propertyKeyPart : propertyKeyParts) {
            if (propertyKeyPart.startsWith("~")) {
                return getDimensionsFromDimensionsContainer(propertyKeyPart);
            }
        }

        return new ArrayList<>();
    }

    private static List<String> getDimensionsFromDimensionsContainer(String propertyKeyPart) {
        List<String> dimensions = new ArrayList<>();

        String [] rawDimensions = propertyKeyPart.split("~");

        for (String rawDimension : rawDimensions) {
            String trimmedRawDimension = rawDimension.trim();

            if (!trimmedRawDimension.isEmpty()) {
                dimensions.add(trimmedRawDimension);
            }
        }

        return dimensions;
    }

    private static String getLowerCaseStartingNamePart(String propertyNamePart) {
        return propertyNamePart.substring(0, 1).toLowerCase() +
                propertyNamePart.substring(1);
    }

    private static String getUpperCaseStartingNamePart(String propertyNamePart) {
        return propertyNamePart.substring(0, 1).toUpperCase() +
                propertyNamePart.substring(1);
    }
}
