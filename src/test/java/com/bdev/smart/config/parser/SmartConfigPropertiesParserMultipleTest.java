package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.AllDimensions;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.data.inner.property.PropertyType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SmartConfigPropertiesParserMultipleTest extends SmartConfigParserTest {
    @Test
    public void testMultipleProperties() {
        AllDimensions allDimensions = new AllDimensions(); {
            allDimensions.addDimension("tier").addValue("sit");
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/multiple",
                        "test-multiple-properties"
                ),
                allDimensions
        );

        assertEquals(2, allProperties.getAllProperties().size());

        assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsProperty().size());
        assertEquals(1, allProperties.getAllProperties().get("a")
                .getDimensionsProperty()
                .stream()
                .filter(it -> it.getType() == PropertyType.NUMBER)
                .filter(it -> it.getDimensions().size() == 1)
                .filter(it -> it.getDimensions().get("tier").equals("sit"))
                .filter(it -> it.getValue().equals(1))
                .count()
        );

        assertEquals(1, allProperties.getAllProperties().get("b").getDimensionsProperty().size());
        assertEquals(1, allProperties.getAllProperties().get("b")
                .getDimensionsProperty()
                .stream()
                .filter(it -> it.getType() == PropertyType.NUMBER)
                .filter(it -> it.getDimensions().size() == 1)
                .filter(it -> it.getDimensions().get("tier").equals("sit"))
                .filter(it -> it.getValue().equals(2))
                .count()
        );
    }
}
