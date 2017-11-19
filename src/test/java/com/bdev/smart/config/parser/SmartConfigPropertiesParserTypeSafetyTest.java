package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.*;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.data.inner.property.PropertyType;
import com.bdev.smart.config.parser.property.SmartConfigPropertiesParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SmartConfigPropertiesParserTypeSafetyTest extends SmartConfigParserTest {
    @Test
    public void testSimpleInteger() {
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
                        "properties-parser/type-safety",
                        "test-type-safety-integer"
                ),
                new SpaceInfo(space, points)
        );

        Assert.assertEquals(1, allProperties.getAllProperties().size());
        Assert.assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsProperty().size());

        Assert.assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsProperty()
                        .stream()
                        .filter(it -> it.getValue().equals(3))
                        .filter(it -> it.getType().equals(PropertyType.NUMBER))
                        .filter(it -> it.getPoint().getLocation().size() == 1)
                        .filter(it -> it.getPoint().containsCoordinate("tier", "sit"))
                        .count()
        );
    }

    @Test
    public void testSimpleString() {
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
                        "properties-parser/type-safety",
                        "test-type-safety-string"
                ),
                new SpaceInfo(space, points)
        );

        Assert.assertEquals(1, allProperties.getAllProperties().size());
        Assert.assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsProperty().size());

        Assert.assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsProperty()
                        .stream()
                        .filter(it -> it.getValue().equals("hello"))
                        .filter(it -> it.getType().equals(PropertyType.STRING))
                        .filter(it -> it.getPoint().getLocation().size() == 1)
                        .filter(it -> it.getPoint().containsCoordinate("tier", "sit"))
                        .count()
        );
    }

    @Test
    public void testSimpleBoolean() {
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
                        "properties-parser/type-safety",
                        "test-type-safety-boolean"
                ),
                new SpaceInfo(space, points)
        );

        Assert.assertEquals(1, allProperties.getAllProperties().size());
        Assert.assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsProperty().size());

        Assert.assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsProperty()
                        .stream()
                        .filter(it -> it.getValue().equals(false))
                        .filter(it -> it.getType().equals(PropertyType.BOOLEAN))
                        .filter(it -> it.getPoint().getLocation().size() == 1)
                        .filter(it -> it.getPoint().containsCoordinate("tier", "sit"))
                        .count()
        );
    }

    @Test
    public void testSimpleListOfStrings() {
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
                        "properties-parser/type-safety",
                        "test-type-safety-list-of-strings"
                ),
                new SpaceInfo(space, points)
        );

        Assert.assertEquals(1, allProperties.getAllProperties().size());
        Assert.assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsProperty().size());

        Assert.assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsProperty()
                        .stream()
                        .filter(it -> ((List) it.getValue()).size() == 3)
                        .filter(it -> ((List) it.getValue()).contains("hello_1"))
                        .filter(it -> ((List) it.getValue()).contains("hello_2"))
                        .filter(it -> ((List) it.getValue()).contains("hello_3"))
                        .filter(it -> it.getType().equals(PropertyType.LIST_OF_STRINGS))
                        .filter(it -> it.getPoint().getLocation().size() == 1)
                        .filter(it -> it.getPoint().containsCoordinate("tier", "sit"))
                        .count()
        );
    }

    @Test
    public void testSimpleListOfNumbers() {
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
                        "properties-parser/type-safety",
                        "test-type-safety-list-of-numbers"
                ),
                new SpaceInfo(space, points)
        );

        Assert.assertEquals(1, allProperties.getAllProperties().size());
        Assert.assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsProperty().size());

        Assert.assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsProperty()
                        .stream()
                        .filter(it -> ((List) it.getValue()).size() == 3)
                        .filter(it -> ((List) it.getValue()).contains(1))
                        .filter(it -> ((List) it.getValue()).contains(2))
                        .filter(it -> ((List) it.getValue()).contains(3))
                        .filter(it -> it.getType().equals(PropertyType.LIST_OF_NUMBERS))
                        .filter(it -> it.getPoint().getLocation().size() == 1)
                        .filter(it -> it.getPoint().containsCoordinate("tier", "sit"))
                        .count()
        );
    }

    @Test(expected = RuntimeException.class)
    public void testSimpleListOfMixed() {
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
                        "properties-parser/type-safety",
                        "test-type-safety-list-of-mixed"
                ),
                new SpaceInfo(space, points)
        );
    }

    @Test(expected = RuntimeException.class)
    public void testTypeSafetyMultipleValuesListOfMixed() {
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
                        "properties-parser/type-safety",
                        "test-type-safety-multiple-values-list-of-mixed"
                ),
                new SpaceInfo(space, points)
        );
    }
}
