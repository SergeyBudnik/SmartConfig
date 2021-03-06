package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.Space;
import com.bdev.smart.config.data.inner.dimension.SpaceInfo;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.data.inner.property.DefaultProperty;
import com.bdev.smart.config.data.inner.property.Property;
import com.bdev.smart.config.parser.property.SmartConfigPropertiesParser;
import org.junit.Test;

import java.util.HashSet;

import static com.bdev.smart.config.data.inner.property.PropertyType.NUMBER;
import static com.bdev.smart.config.data.inner.property.PropertyType.STRING;
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
    public void testDashNaming() {
        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/naming",
                        "test-dash-naming"
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
        assertEquals(NUMBER, allProperties.getAllProperties().get("testPropertyNameMixed").getType());
        assertEquals(0, allProperties.getAllProperties().get("testPropertyNameMixed").getDimensionsProperty().size());

        DefaultProperty defaultProperty = allProperties.getAllProperties().get("testPropertyNameMixed").getDefaultProperty();

        assertEquals(1, defaultProperty.getValue());
        assertEquals(NUMBER, defaultProperty.getType());
    }

    @Test
    public void testTemporaryNaming() {
        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/naming",
                        "test-temporary-naming"
                ),
                new SpaceInfo(new Space(), new HashSet<>())
        );

        assertEquals(1, allProperties.getAllProperties().size());

        Property property = allProperties.getAllProperties().get("testPropertyNameTemporaryUsage");

        assertEquals(STRING, property.getType());
        assertEquals(0, property.getDimensionsProperty().size());

        DefaultProperty defaultProperty = property.getDefaultProperty();

        assertEquals("1_2", defaultProperty.getValue());
        assertEquals(STRING, defaultProperty.getType());
    }
}
