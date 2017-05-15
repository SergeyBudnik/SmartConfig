package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.AllDimensions;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.data.inner.property.DefaultProperty;
import com.bdev.smart.config.data.inner.property.PropertyType;
import org.junit.Test;

import static org.junit.Assert.*;

public class SmartConfigPropertiesParserDefaultTest extends SmartConfigParserTest {
    @Test
    public void testPropertyWithDefault() {
        AllDimensions allDimensions = new AllDimensions(); {
            allDimensions.addDimension("tier").addValue("sit");
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/default",
                        "test-property-with-default"
                ),
                allDimensions
        );

        assertEquals(1, allProperties.getAllProperties().size());
        assertEquals(0, allProperties.getAllProperties().get("a").getDimensionsProperty().size());

        DefaultProperty defaultProperty = allProperties.getAllProperties().get("a").getDefaultProperty(); {
            assertEquals(PropertyType.NUMBER, defaultProperty.getType());
            assertEquals(3, defaultProperty.getValue());
        }
    }
}
