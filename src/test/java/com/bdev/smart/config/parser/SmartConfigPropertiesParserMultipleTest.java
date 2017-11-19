package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.*;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.data.inner.property.PropertyType;
import com.bdev.smart.config.parser.property.SmartConfigPropertiesParser;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SmartConfigPropertiesParserMultipleTest extends SmartConfigParserTest {
    @Test
    public void testMultipleProperties() {
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
                        "properties-parser/multiple",
                        "test-multiple-properties"
                ),
                new SpaceInfo(space, points)
        );

        assertEquals(2, allProperties.getAllProperties().size());

        assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsProperty().size());
        assertEquals(1, allProperties.getAllProperties().get("a")
                .getDimensionsProperty()
                .stream()
                .filter(it -> it.getType() == PropertyType.NUMBER)
                .filter(it -> it.getPoint().getLocation().size() == 1)
                .filter(it -> it.getPoint().containsCoordinate("tier", "sit"))
                .filter(it -> it.getValue().equals(1))
                .count()
        );

        assertEquals(1, allProperties.getAllProperties().get("b").getDimensionsProperty().size());
        assertEquals(1, allProperties.getAllProperties().get("b")
                .getDimensionsProperty()
                .stream()
                .filter(it -> it.getType() == PropertyType.NUMBER)
                .filter(it -> it.getPoint().getLocation().size() == 1)
                .filter(it -> it.getPoint().containsCoordinate("tier", "sit"))
                .filter(it -> it.getValue().equals(2))
                .count()
        );
    }
}
