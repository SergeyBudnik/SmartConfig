package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.AllDimensions;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.parser.property.SmartConfigPropertiesParser;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SmartConfigPropertiesModifiersTest extends SmartConfigParserTest {
    @Test
    public void testReadProtectedModifier() {
        AllDimensions allDimensions = new AllDimensions(); {
            allDimensions.addDimension("tier").addValue("sit");
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/modifiers",
                        "test-read-modifier"
                ),
                allDimensions
        );

        assertFalse(allProperties.getAllProperties().get("a").isReadProtected());
        assertTrue(allProperties.getAllProperties().get("b").isOverrideProtected());

        assertTrue(allProperties.getAllProperties().get("b").isReadProtected());
        assertTrue(allProperties.getAllProperties().get("b").isOverrideProtected());
    }

    @Test
    public void testOverrideProtectedModifier() {
        AllDimensions allDimensions = new AllDimensions(); {
            allDimensions.addDimension("tier").addValue("sit");
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/modifiers",
                        "test-override-modifier"
                ),
                allDimensions
        );

        assertFalse(allProperties.getAllProperties().get("a").isOverrideProtected());
        assertTrue(allProperties.getAllProperties().get("b").isReadProtected());

        assertTrue(allProperties.getAllProperties().get("b").isOverrideProtected());
        assertTrue(allProperties.getAllProperties().get("b").isReadProtected());
    }
}
