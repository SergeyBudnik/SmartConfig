package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.*;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.data.inner.property.DefaultProperty;
import com.bdev.smart.config.data.inner.property.PropertyType;
import com.bdev.smart.config.parser.property.SmartConfigPropertiesParser;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SmartConfigPropertiesParserDefaultTest extends SmartConfigParserTest {
    @Test
    public void testPropertyWithDefault() {
        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/default",
                        "test-property-with-default"
                ),
                new SpaceInfo(new Space(), new HashSet<>())
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
                new SpaceInfo(new Space(), new HashSet<>())
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
                        "properties-parser/default",
                        "test-property-with-values-and-default"
                ),
                new SpaceInfo(space, points)
        );

        assertEquals(1, allProperties.getAllProperties().size());

        assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsProperty().size());
        assertEquals(PropertyType.NUMBER, allProperties.getAllProperties().get("a").getType());
        assertEquals(1, allProperties.getAllProperties().get("a")
                .getDimensionsProperty()
                .stream()
                .filter(it -> it.getValue().equals(1))
                .filter(it -> it.getType().equals(PropertyType.NUMBER))
                .filter(it -> it.getPoint().getLocation().size() == 1)
                .filter(it -> it.getPoint().getLocation().get(new Dimension("tier")).equals(new DimensionValue("sit")))
                .count()
        );

        DefaultProperty defaultProperty = allProperties.getAllProperties().get("a").getDefaultProperty(); {
            assertEquals(PropertyType.NUMBER, defaultProperty.getType());
            assertEquals(3, defaultProperty.getValue());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testPropertyWithDefaultTypesConflict() {
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

        SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/default",
                        "test-property-with-default-types-conflict"
                ),
                new SpaceInfo(space, points)
        );
    }
}
