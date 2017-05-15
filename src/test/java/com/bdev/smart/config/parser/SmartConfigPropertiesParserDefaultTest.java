package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.AllDimensions;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.data.inner.property.DefaultProperty;
import com.bdev.smart.config.data.inner.property.PropertyType;
import com.bdev.smart.config.parser.property.SmartConfigPropertiesParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class SmartConfigPropertiesParserDefaultTest extends SmartConfigParserTest {
    @Test
    public void testPropertyWithDefault() {
        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/default",
                        "test-property-with-default"
                ),
                new AllDimensions()
        );

        assertEquals(1, allProperties.getAllProperties().size());
        assertEquals(PropertyType.NUMBER, allProperties.getAllProperties().get("a").getType());
        assertEquals(0, allProperties.getAllProperties().get("a").getDimensionsProperty().size());

        DefaultProperty defaultProperty = allProperties.getAllProperties().get("a").getDefaultProperty(); {
            assertEquals(PropertyType.NUMBER, defaultProperty.getType());
            assertEquals(3, defaultProperty.getValue());
        }
    }

    @Test
    public void testPropertyWithInlineDefault() {
        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/default",
                        "test-property-with-inline-default"
                ),
                new AllDimensions()
        );

        assertEquals(1, allProperties.getAllProperties().size());
        assertEquals(PropertyType.NUMBER, allProperties.getAllProperties().get("a").getType());
        assertEquals(0, allProperties.getAllProperties().get("a").getDimensionsProperty().size());

        DefaultProperty defaultProperty = allProperties.getAllProperties().get("a").getDefaultProperty(); {
            assertEquals(PropertyType.NUMBER, defaultProperty.getType());
            assertEquals(3, defaultProperty.getValue());
        }
    }

    @Test
    public void testPropertyWithValueAndDefault() {
        AllDimensions allDimensions = new AllDimensions(); {
            allDimensions.addDimension("tier").addValue("sit");
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/default",
                        "test-property-with-values-and-default"
                ),
                allDimensions
        );

        assertEquals(1, allProperties.getAllProperties().size());

        assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsProperty().size());
        assertEquals(PropertyType.NUMBER, allProperties.getAllProperties().get("a").getType());
        assertEquals(1, allProperties.getAllProperties().get("a")
                .getDimensionsProperty()
                .stream()
                .filter(it -> it.getValue().equals(1))
                .filter(it -> it.getType().equals(PropertyType.NUMBER))
                .filter(it -> it.getDimensions().size() == 1)
                .filter(it -> it.getDimensions().get("tier").equals("sit"))
                .count()
        );

        DefaultProperty defaultProperty = allProperties.getAllProperties().get("a").getDefaultProperty(); {
            assertEquals(PropertyType.NUMBER, defaultProperty.getType());
            assertEquals(3, defaultProperty.getValue());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testPropertyWithDefaultTypesConflict() {
        AllDimensions allDimensions = new AllDimensions(); {
            allDimensions.addDimension("tier").addValue("sit");
        }

        SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/default",
                        "test-property-with-default-types-conflict"
                ),
                allDimensions
        );
    }
}
