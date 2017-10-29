package com.bdev.smart.config.parser.property;

import com.bdev.smart.config.data.inner.dimension.AllDimensions;
import com.bdev.smart.config.data.inner.dimension.Dimension;
import com.bdev.smart.config.data.inner.property.*;
import com.bdev.smart.config.exceptions.PropertyIsInvalid;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

import java.io.File;
import java.util.List;

import static com.bdev.smart.config.parser.property.SmartConfigPropertyResolver.getDimensions;
import static com.bdev.smart.config.parser.property.SmartConfigPropertyResolver.getName;

public class SmartConfigPropertiesParser {
    private static final String DEFAULT_PROPERTY_KEYWORD = "default";
    private static final String MODIFIERS_PROPERTY_KEYWORD = "modifiers";

    private static final String MODIFIER_UNSAFE_READ = "-r";
    private static final String MODIFIER_UNSAFE_OVERRIDE = "-o";

    public static AllProperties parse(
            String csvPropertiesPath,
            AllDimensions allDimensions
    ) {
        String [] propertiesPaths = csvPropertiesPath.split(",");

        Config config = null;

        if (propertiesPaths.length == 0) {
            throw new RuntimeException("At least one property file must exist");
        }

        for (String propertiesPath : propertiesPaths) {
            Config unresolvedConfig = ConfigFactory.parseFile(new File(propertiesPath));

            config = config == null ?
                    unresolvedConfig.resolve() :
                    unresolvedConfig.withFallback(config).resolve();
        }

        return getAllProperties(config, allDimensions);
    }

    private static AllProperties getAllProperties(Config config, AllDimensions allDimensions) {
        AllProperties allProperties = new AllProperties();

        config
                .entrySet()
                .forEach(configProperty -> {
                    String propertyName = getName(configProperty.getKey());
                    List<String> dimensionsValues = getDimensions(configProperty.getKey());

                    Property property = allProperties.findOrCreateProperty(propertyName);
                    Object propertyValue = config.getAnyRef(configProperty.getKey());

                    if (dimensionsValues.contains(MODIFIERS_PROPERTY_KEYWORD)) {
                        processModifiers(property, (String) propertyValue);
                    } else {
                        PropertyType propertyType = getType(propertyName, configProperty.getValue(), propertyValue);

                        if (dimensionsValues.size() == 0 || dimensionsValues.contains(DEFAULT_PROPERTY_KEYWORD)) {
                            processDefaultProperty(dimensionsValues, property, propertyName, propertyValue, propertyType);
                        } else {
                            processProperty(property, allDimensions, allProperties, propertyName, propertyValue, propertyType, dimensionsValues);
                        }
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
            AllDimensions allDimensions,
            AllProperties allProperties,
            String propertyName,
            Object propertyValue,
            PropertyType propertyType,
            List<String> dimensionsValues
    ) {
        DimensionProperty dimensionProperty = getDimensionProperty(
                property,
                allDimensions,
                propertyName,
                propertyValue,
                propertyType,
                dimensionsValues
        );

        allProperties
                .findOrCreateProperty(propertyName)
                .addDimensionProperty(dimensionProperty);
    }

    private static void processModifiers(Property property, String value) {
        if (value.contains(MODIFIER_UNSAFE_READ)) {
            property.setReadProtected(false);
        }

        if (value.contains(MODIFIER_UNSAFE_OVERRIDE)) {
            property.setOverrideProtected(false);
        }
    }

    private static DimensionProperty getDimensionProperty(
            Property property,
            AllDimensions allDimensions,
            String propertyName,
            Object propertyValue,
            PropertyType propertyType,
            List<String> dimensionsValues
    ) {
        DimensionProperty dimensionProperty = new DimensionProperty(
                property,
                propertyName,
                propertyValue,
                propertyType
        ); {
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
