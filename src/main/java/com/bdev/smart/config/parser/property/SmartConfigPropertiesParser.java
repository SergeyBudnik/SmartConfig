package com.bdev.smart.config.parser.property;

import com.bdev.smart.config.data.inner.dimension.Dimension;
import com.bdev.smart.config.data.inner.dimension.DimensionValue;
import com.bdev.smart.config.data.inner.dimension.Point;
import com.bdev.smart.config.data.inner.dimension.SpaceInfo;
import com.bdev.smart.config.data.inner.property.*;
import com.bdev.smart.config.exceptions.PropertyIsInvalid;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.bdev.smart.config.parser.property.SmartConfigPropertyResolver.getDimensions;
import static com.bdev.smart.config.parser.property.SmartConfigPropertyResolver.getName;

public class SmartConfigPropertiesParser {
    private static final String DEFAULT_PROPERTY_KEYWORD = "default";

    public static AllProperties parse(String csvPropertiesPath, SpaceInfo spaceInfo) {
        String [] propertiesPaths = csvPropertiesPath.split(",");

        Config config = null;

        if (propertiesPaths.length == 0) {
            throw new RuntimeException("At least one property file must exist");
        }

        for (String propertiesPath : propertiesPaths) {
            Config unresolvedConfig = ConfigFactory.parseFile(new File(propertiesPath.trim()));

            config = config == null ?
                    unresolvedConfig.resolve() :
                    unresolvedConfig.withFallback(config).resolve();
        }

        return getAllProperties(config, spaceInfo);
    }

    private static AllProperties getAllProperties(Config config, SpaceInfo spaceInfo) {
        AllProperties allProperties = new AllProperties();

        config
                .entrySet()
                .forEach(configProperty -> {
                    String propertyName = getName(configProperty.getKey());
                    List<String> dimensionsValues = getDimensions(configProperty.getKey());

                    Property property = allProperties.findOrCreateProperty(propertyName);
                    Object propertyValue = config.getAnyRef(configProperty.getKey());

                    PropertyType propertyType = getType(propertyName, configProperty.getValue(), propertyValue);

                    if (dimensionsValues.size() == 0 || dimensionsValues.contains(DEFAULT_PROPERTY_KEYWORD)) {
                        processDefaultProperty(dimensionsValues, property, propertyName, propertyValue, propertyType);
                    } else {
                        processProperty(property, spaceInfo, allProperties, propertyName, propertyValue, propertyType, dimensionsValues);
                    }
                });

        return allProperties;
    }

    private static void processDefaultProperty(
            List<String> dimensionsValues,
            Property property,
            String propertyName,
            Object propertyValue,
            PropertyType propertyType
    ) {
        if (dimensionsValues.size() > 1) {
            throw new PropertyIsInvalid(propertyName);
        }

        DefaultProperty defaultProperty = new DefaultProperty(
                property,
                propertyName,
                propertyValue,
                propertyType
        );

        property.setDefaultProperty(defaultProperty);
    }

    private static void processProperty(
            Property property,
            SpaceInfo spaceInfo,
            AllProperties allProperties,
            String propertyName,
            Object propertyValue,
            PropertyType propertyType,
            List<String> dimensionsValues
    ) {
        PointProperty dimensionProperty = new PointProperty(
                property,
                propertyName,
                propertyValue,
                propertyType,
                point(propertyName, spaceInfo, dimensionsValues)
        );

        allProperties
                .findOrCreateProperty(propertyName)
                .addDimensionProperty(dimensionProperty);
    }

    private static Point point(String propertyName, SpaceInfo spaceInfo, List<String> dimensionsValues) {
        Point point = new Point();

        for (String dimensionValueName : dimensionsValues) {
            DimensionValue dimensionValue = new DimensionValue(dimensionValueName);
            Optional<Dimension> dimension = spaceInfo.getSpace().getDimensionByValue(dimensionValue);

            if (!dimension.isPresent()) {
                String coordinates = dimensionsValues
                        .stream()
                        .reduce((d1, d2) -> d1 + "." + d2)
                        .orElse("");

                throw new RuntimeException(String.format(
                        "Invalid property: %s. Point with coordinates '%s' does not exist in space",
                        propertyName,
                        coordinates
                ));
            }

            point.addCoordinate(dimension.get(), dimensionValue);
        }

        return point;
    }

    private static PropertyType getType(String propertyName, ConfigValue configValue, Object value) {
        switch (configValue.valueType()) {
            case BOOLEAN:
                return PropertyType.BOOLEAN;
            case NUMBER:
                return PropertyType.NUMBER;
            case STRING:
                return PropertyType.STRING;
            case LIST:
                return getListType(propertyName, (List) value);
        }

        throw new PropertyIsInvalid(propertyName);
    }

    private static PropertyType getListType(String propertyName, List value) {
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
                throw new PropertyIsInvalid(propertyName);
            }

            if (type == null) {
                type = currentType;
            } else {
                if (type != currentType) {
                    throw new PropertyIsInvalid(propertyName);
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
}
