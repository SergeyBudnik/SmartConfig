package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.*;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.data.inner.property.PropertyType;
import com.bdev.smart.config.parser.property.SmartConfigPropertiesParser;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SmartConfigPropertiesParserDimensionsTest extends SmartConfigParserTest {
    @Test
    public void testPropertyWithSingleValueWithSingleDimension() {
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
                        "properties-parser/dimension",
                        "test-property-with-single-value-with-single-dimension"
                ),
                new SpaceInfo(space, points)
        );

        assertEquals(1, allProperties.getAllProperties().size());
        assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsProperty().size());

        assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsProperty()
                        .stream()
                        .filter(it -> it.getValue().equals(1))
                        .filter(it -> it.getType().equals(PropertyType.NUMBER))
                        .filter(it -> it.getPoint().getLocation().size() == 1)
                        .filter(it -> it.getPoint().getLocation().get(new Dimension("tier")).equals(new DimensionValue("sit")))
                        .count()
        );
    }

    @Test
    public void testPropertyWithSingleValueWithMultipleDimensions() {
        Space space = new Space(); {
            Dimension tierDimension = new Dimension("tier"); {
                tierDimension.addValue(new DimensionValue("sit"));

                space.addDimension(tierDimension);
            }

            Dimension zoneDimension = new Dimension("zone"); {
                zoneDimension.addValue(new DimensionValue("uk"));

                space.addDimension(zoneDimension);
            }
        }

        Set<Point> points = new HashSet<>(); {
            Point sitPoint = new Point(); {
                sitPoint.getLocation().put(new Dimension("tier"), new DimensionValue("sit"));
                sitPoint.getLocation().put(new Dimension("zone"), new DimensionValue("uk"));

                points.add(sitPoint);
            }
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/dimension",
                        "test-property-with-single-value-with-multiple-dimensions"
                ),
                new SpaceInfo(space, points)
        );

        assertEquals(1, allProperties.getAllProperties().size());
        assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsProperty().size());

        assertEquals(1, allProperties.getAllProperties().get("a")
                .getDimensionsProperty()
                .stream()
                .filter(it -> it.getValue().equals(1))
                .filter(it -> it.getType().equals(PropertyType.NUMBER))
                .filter(it -> it.getPoint().getLocation().size() == 2)
                .filter(it -> it.getPoint().getLocation().get(new Dimension("tier")).equals(new DimensionValue("sit")))
                .filter(it -> it.getPoint().getLocation().get(new Dimension("zone")).equals(new DimensionValue("uk")))
                .count()
        );
    }

    @Test
    public void testPropertyWithMultipleValues() {
        Space space = new Space(); {
            Dimension tierDimension = new Dimension("tier"); {
                tierDimension.addValue(new DimensionValue("sit"));
                tierDimension.addValue(new DimensionValue("uat"));
                tierDimension.addValue(new DimensionValue("prod"));

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
                        "properties-parser/dimension",
                        "test-property-with-multiple-values"
                ),
                new SpaceInfo(space, points)
        );

        assertEquals(1, allProperties.getAllProperties().size());
        assertEquals(3, allProperties.getAllProperties().get("a").getDimensionsProperty().size());

        assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsProperty()
                        .stream()
                        .filter(it -> it.getValue().equals(1))
                        .filter(it -> it.getType().equals(PropertyType.NUMBER))
                        .filter(it -> it.getPoint().getLocation().size() == 1)
                        .filter(it -> it.getPoint().getLocation().get(new Dimension("tier")).equals(new DimensionValue("sit")))
                        .count()
        );

        assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsProperty()
                        .stream()
                        .filter(it -> it.getValue().equals(2))
                        .filter(it -> it.getType().equals(PropertyType.NUMBER))
                        .filter(it -> it.getPoint().getLocation().size() == 1)
                        .filter(it -> it.getPoint().getLocation().get(new Dimension("tier")).equals(new DimensionValue("uat")))
                        .count()
        );

        assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsProperty()
                        .stream()
                        .filter(it -> it.getValue().equals(3))
                        .filter(it -> it.getType().equals(PropertyType.NUMBER))
                        .filter(it -> it.getPoint().getLocation().size() == 1)
                        .filter(it -> it.getPoint().getLocation().get(new Dimension("tier")).equals(new DimensionValue("prod")))
                        .count()
        );
    }

    @Test(expected = RuntimeException.class)
    public void testPropertyWithUnrecognizedDimension() {
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
                        "properties-parser/dimension",
                        "test-property-with-unrecognized-dimension"
                ),
                new SpaceInfo(space, points)
        );
    }

    @Test(expected = RuntimeException.class)
    public void testPropertyWithSingleValueWithMultipleDimensionsWithConflict() {
        Space space = new Space(); {
            Dimension tierDimension = new Dimension("tier"); {
                tierDimension.addValue(new DimensionValue("sit"));
                tierDimension.addValue(new DimensionValue("uat"));

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
                        "properties-parser/dimension",
                        "test-property-with-single-value-with-multiple-dimensions-with-conflict"
                ),
                new SpaceInfo(space, points)
        );
    }
}
