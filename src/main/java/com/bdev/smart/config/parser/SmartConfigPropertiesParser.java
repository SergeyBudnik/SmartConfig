package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.AllDimensions;
import com.bdev.smart.config.data.inner.dimension.Dimension;
import com.bdev.smart.config.data.inner.property.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class SmartConfigPropertiesParser {
    private static final String DEFAULT_PROPERTY_KEYWORD = "default";

    static AllProperties parse(
            String propertiesPath,
            AllDimensions allDimensions
    ) {
        Config config = ConfigFactory.parseFile(new File(propertiesPath));

        return getAllProperties(config, allDimensions);
    }

    private static AllProperties getAllProperties(Config config, AllDimensions allDimensions) {
        AllProperties allProperties = new AllProperties();

        config
                .entrySet()
                .forEach(configProperty -> {
                    String [] propertyKeyParts = splitKey(configProperty.getKey());

                    String propertyName = extractPropertyName(propertyKeyParts);

                    List<String> dimensionsValues = extractDimensions(propertyKeyParts);

                    Property property = allProperties.findOrCreateProperty(propertyName);

                    Object propertyValue = config.getAnyRef(configProperty.getKey());
                    PropertyType propertyType = getType(configProperty.getValue(), propertyValue);

                    if (dimensionsValues.contains(DEFAULT_PROPERTY_KEYWORD)) {
                        if (dimensionsValues.size() != 1) {
                            throw new RuntimeException();
                        }

                        DefaultProperty defaultProperty = new DefaultProperty(propertyValue, propertyType);

                        property.setDefaultProperty(defaultProperty);
                    } else {
                        DimensionProperty dimensionProperty = getDimensionProperty(
                                allDimensions,
                                propertyValue,
                                propertyType,
                                dimensionsValues
                        );

                        allProperties
                                .findOrCreateProperty(propertyName)
                                .addDimensionProperty(dimensionProperty);
                    }
                });

        return allProperties;
    }

    private static DimensionProperty getDimensionProperty(
            AllDimensions allDimensions,
            Object propertyValue,
            PropertyType propertyType,
            List<String> dimensionsValues
    ) {
        DimensionProperty dimensionProperty = new DimensionProperty(propertyValue, propertyType); {
            for (String dimensionValue : dimensionsValues) {
                Dimension dimension = allDimensions.findDimensionByValue(dimensionValue);

                dimensionProperty.addDimension(
                        dimension.getName(),
                        dimensionValue
                );
            }
        }

        return dimensionProperty;
    }

    private static PropertyType getType(ConfigValue configValue, Object value) {
        switch (configValue.valueType()) {
            case BOOLEAN:
                return PropertyType.BOOLEAN;
            case NUMBER:
                return PropertyType.NUMBER;
            case STRING:
                return PropertyType.STRING;
            case LIST:
                return getListType((List) value);
        }

        throw new RuntimeException();
    }

    private static PropertyType getListType(List value) {
        PropertyType type = null;

        for (Object o : value) {
            PropertyType currentType;

            if (isNumber(o)) {
                currentType = PropertyType.LIST_OF_NUMBERS;
            } else if (o instanceof Boolean) {
                currentType = PropertyType.LIST_OF_BOOLEANS;
            } else if (o instanceof String) {
                currentType = PropertyType.LIST_OF_STRINGS;
            } else {
                throw new RuntimeException();
            }

            if (type == null) {
                type = currentType;
            } else {
                if (type != currentType) {
                    throw new RuntimeException();
                }
            }
        }

        return type;
    }

    private static boolean isNumber(Object o) {
        return
                (o instanceof Byte) ||
                (o instanceof Short) ||
                (o instanceof Integer) ||
                (o instanceof Long) ||
                (o instanceof Float) ||
                (o instanceof Double);
    }

    private static String [] splitKey(String key) {
        return key.split("\\.");
    }

    private static String extractPropertyName(String [] propertyKeyParts) {
        return propertyKeyParts[0];
    }

    private static List<String> extractDimensions(String [] propertyKeyParts) {
        List<String> dimensions = new ArrayList<>();

        String [] rawDimensions = propertyKeyParts[1].replace("\"", "").split("~");

        for (String rawDimension : rawDimensions) {
            String trimmedRawDimension = rawDimension.trim();

            if (!trimmedRawDimension.isEmpty()) {
                dimensions.add(trimmedRawDimension);
            }
        }

        return dimensions;
    }
}
