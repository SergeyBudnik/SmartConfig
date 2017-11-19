package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.*;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.parser.property.SmartConfigPropertiesParser;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SmartConfigPropertiesModifiersTest extends SmartConfigParserTest {
    @Test
    public void testReadProtectedModifier() {
        Space space = new Space(); {
            Dimension tierDimension = new Dimension("tier"); {
                tierDimension.addValue(new DimensionValue("sit"));

                space.addDimension(tierDimension);
            }
        }

        Set<Point> points = new HashSet<>(); {
            Point sitPoint = new Point(); {
                points.add(sitPoint);
            }
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/modifiers",
                        "test-read-modifier"
                ),
                new SpaceInfo(space, points)
        );

        assertFalse(allProperties.getAllProperties().get("a").isReadProtected());
        assertTrue(allProperties.getAllProperties().get("b").isOverrideProtected());

        assertTrue(allProperties.getAllProperties().get("b").isReadProtected());
        assertTrue(allProperties.getAllProperties().get("b").isOverrideProtected());
    }

    @Test
    public void testOverrideProtectedModifier() {
        Space space = new Space(); {
            Dimension tierDimension = new Dimension("tier"); {
                tierDimension.addValue(new DimensionValue("sit"));

                space.addDimension(tierDimension);
            }
        }

        Set<Point> points = new HashSet<>(); {
            Point sitPoint = new Point(); {
                points.add(sitPoint);
            }
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/modifiers",
                        "test-override-modifier"
                ),
                new SpaceInfo(space, points)
        );

        assertFalse(allProperties.getAllProperties().get("a").isOverrideProtected());
        assertTrue(allProperties.getAllProperties().get("b").isReadProtected());

        assertTrue(allProperties.getAllProperties().get("b").isOverrideProtected());
        assertTrue(allProperties.getAllProperties().get("b").isReadProtected());
    }
}
