package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.DimensionInfo;
import com.bdev.smart.config.data.inner.DimensionPropertyInfo;
import com.bdev.smart.config.data.inner.PropertyInfo;
import com.bdev.smart.config.data.inner.PropertyType;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

class SmartConfigPropertiesParser {
    static Map<String, PropertyInfo> parse(
            String propertiesPath,
            Map<String, DimensionInfo> allDimensions
    ) {
        Map<String, PropertyInfo> res =
                getCollapsedProperties(getRawProperties(propertiesPath));

        res.forEach((propertyName, propertyInfo) -> propertyInfo
                .getDimensionsPropertyInfo()
                .stream()
                .map(DimensionPropertyInfo::getDimensions)
                .forEach(dimensionsNames ->
                        dimensionsNames.forEach(dimensionName -> {
                            AtomicBoolean dimensionNameFound = new AtomicBoolean(false);

                            allDimensions.forEach((allDimensionsElementName, allDimensionsElementValue) -> {
                                    long matchAmount = allDimensionsElementValue
                                            .getDimensions()
                                            .stream()
                                            .filter(it -> it.equals(dimensionName))
                                            .count();

                                    if (matchAmount == 1) {
                                        dimensionNameFound.set(true);
                                    } else if (matchAmount > 1) {
                                        throw new RuntimeException();
                                    }
                            });

                            if (!dimensionNameFound.get()) {
                                throw new RuntimeException();
                            }
                        })
                ));

        return res;
    }

    private static Map<String, PropertyInfo> getCollapsedProperties(
            Map<String, List<DimensionPropertyInfo>> rawProperties
    ) {
        Map<String, PropertyInfo> collapsedProperties = new HashMap<>();

        for (String propertyName : rawProperties.keySet()) {
            PropertyType type = null;

            for (DimensionPropertyInfo dimensionPropertyInfo : rawProperties.get(propertyName)) {
                if (type == null) {
                    type = dimensionPropertyInfo.getType();
                }
            }

            PropertyInfo propertyInfo = new PropertyInfo(); {
                propertyInfo.setType(type);
                propertyInfo.setDimensionsPropertyInfo(rawProperties.get(propertyName));
            }

            collapsedProperties.put(propertyName, propertyInfo);
        }

        return collapsedProperties;
    }

    private static Map<String, List<DimensionPropertyInfo>> getRawProperties(String propertiesPath) {
        Config config = ConfigFactory.parseFile(new File(propertiesPath));

        Map<String, List<DimensionPropertyInfo>> rawProperties = new HashMap<>();

        config
                .entrySet()
                .forEach(property -> {
                    String [] propertyKeyParts = splitKey(property.getKey());

                    String propertyName = extractPropertyName(propertyKeyParts);
                    List<String> dimensions = extractDimensions(propertyKeyParts);

                    DimensionPropertyInfo dimensionPropertyInfo = new DimensionPropertyInfo(); {
                        Object value = config.getAnyRef(property.getKey());
                        PropertyType propertyType = getType(property.getValue(), value);

                        dimensionPropertyInfo.setDimensions(new HashSet<>(dimensions));
                        dimensionPropertyInfo.setValue(value);
                        dimensionPropertyInfo.setType(propertyType);
                    }

                    List<DimensionPropertyInfo> dimensionsPropertyInfo = rawProperties
                            .getOrDefault(propertyName, new ArrayList<>());

                    dimensionsPropertyInfo.add(dimensionPropertyInfo);

                    rawProperties.put(propertyName, dimensionsPropertyInfo);
                });

        return rawProperties;
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

        for (int i = 1; i < propertyKeyParts.length; i++) {
            dimensions.add(normalizeDimension(propertyKeyParts[i]));
        }

        return dimensions;
    }

    private static String normalizeDimension(String s) {
        return s.replace('\"', ' ').replace('~', ' ').trim();
    }
}
