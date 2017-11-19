package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.Space;
import com.bdev.smart.config.data.inner.dimension.SpaceInfo;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.data.inner.property.DefaultProperty;
import com.bdev.smart.config.parser.property.SmartConfigPropertiesParser;
import org.junit.Test;

import java.util.HashSet;

import static com.bdev.smart.config.data.inner.property.PropertyType.NUMBER;
import static org.junit.Assert.assertEquals;

public class SmartConfigPropertiesParserNamingTest extends SmartConfigParserTest {
    @Test
    public void testCamelCaseNaming() {
        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/naming",
                        "test-camel-case-naming"
                ),
                new SpaceInfo(new Space(), new HashSet<>())
        );

        assertEquals(1, allProperties.getAllProperties().size());
        assertEquals(NUMBER, allProperties.getAllProperties().get("testPropertyName").getType());
        assertEquals(0, allProperties.getAllProperties().get("testPropertyName").getDimensionsProperty().size());

        DefaultProperty defaultProperty = allProperties.getAllProperties().get("testPropertyName").getDefaultProperty();

        assertEquals(1, defaultProperty.getValue());
        assertEquals(NUMBER, defaultProperty.getType());
    }

    @Test
    public void testDotNaming() {
        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/naming",
                        "test-dot-naming"
                ),
                new SpaceInfo(new Space(), new HashSet<>())
        );

        assertEquals(1, allProperties.getAllProperties().size());
        assertEquals(NUMBER, allProperties.getAllProperties().get("testPropertyName").getType());
        assertEquals(0, allProperties.getAllProperties().get("testPropertyName").getDimensionsProperty().size());

        DefaultProperty defaultProperty = allProperties.getAllProperties().get("testPropertyName").getDefaultProperty();

        assertEquals(1, defaultProperty.getValue());
        assertEquals(NUMBER, defaultProperty.getType());
    }

    @Test
    public void testMixedNaming() {
        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/naming",
                        "test-mixed-naming"
                ),
                new SpaceInfo(new Space(), new HashSet<>())
        );

        assertEquals(1, allProperties.getAllProperties().size());
        assertEquals(NUMBER, allProperties.getAllProperties().get("testPropertyName").getType());
        assertEquals(0, allProperties.getAllProperties().get("testPropertyName").getDimensionsProperty().size());

        DefaultProperty defaultProperty = allProperties.getAllProperties().get("testPropertyName").getDefaultProperty();

        assertEquals(1, defaultProperty.getValue());
        assertEquals(NUMBER, defaultProperty.getType());
    }
}
