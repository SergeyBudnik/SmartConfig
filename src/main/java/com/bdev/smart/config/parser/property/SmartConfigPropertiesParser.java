package com.bdev.smart.config.parser.property;

import com.bdev.smart.config.data.inner.dimension.AllDimensions;
import com.bdev.smart.config.data.inner.dimension.Dimension;
import com.bdev.smart.config.data.inner.property.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

import java.io.File;
import java.util.List;

import static com.bdev.smart.config.parser.property.SmartConfigPropertyResolver.getDimensions;
import static com.bdev.smart.config.parser.property.SmartConfigPropertyResolver.getName;

public class SmartConfigPropertiesParser {
    private static final String DEFAULT_PROPERTY_KEYWORD = "default";

    public static AllProperties parse(
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
                    String propertyName = getName(configProperty.getKey());
                    List<String> dimensionsValues = getDimensions(configProperty.getKey());

                    Property property = allProperties.findOrCreateProperty(propertyName);

                    Object propertyValue = config.getAnyRef(configProperty.getKey());
                    PropertyType propertyType = getType(configProperty.getValue(), propertyValue);

                    if (dimensionsValues.size() == 0 || dimensionsValues.contains(DEFAULT_PROPERTY_KEYWORD)) {
                        if (dimensionsValues.size() > 1) {
                            throw new RuntimeException();
                        }

                        DefaultProperty defaultProperty = new DefaultProperty(
                                propertyName,
                                propertyValue,
                                propertyType
                        );

                        property.setDefaultProperty(defaultProperty);
                    } else {
                        DimensionProperty dimensionProperty = getDimensionProperty(
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
                });

        return allProperties;
    }

    private static DimensionProperty getDimensionProperty(
            AllDimensions allDimensions,
            String propertyName,
            Object propertyValue,
            PropertyType propertyType,
            List<String> dimensionsValues
    ) {
        DimensionProperty dimensionProperty = new DimensionProperty(
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
}
