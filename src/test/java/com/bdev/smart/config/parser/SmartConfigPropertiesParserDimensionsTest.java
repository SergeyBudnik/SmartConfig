package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.AllDimensions;
import com.bdev.smart.config.data.inner.dimension.Dimension;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.data.inner.property.PropertyType;
import org.junit.Assert;
import org.junit.Test;

public class SmartConfigPropertiesParserDimensionsTest extends SmartConfigParserTest {
    @Test
    public void testPropertyWithSingleValueWithSingleDimension() {
        AllDimensions allDimensions = new AllDimensions(); {
            allDimensions.addDimension("tier").addValue("sit");
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/dimension",
                        "test-property-with-single-value-with-single-dimension"
                ),
                allDimensions
        );

        Assert.assertEquals(1, allProperties.getAllProperties().size());
        Assert.assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsPropertyInfo().size());

        Assert.assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsPropertyInfo()
                        .stream()
                        .filter(it -> it.getValue().equals(1))
                        .filter(it -> it.getType().equals(PropertyType.NUMBER))
                        .filter(it -> it.getDimensions().size() == 1)
                        .filter(it -> it.getDimensions().contains("sit"))
                        .count()
        );
    }

    @Test
    public void testPropertyWithSingleValueWithMultipleDimensions() {
        AllDimensions allDimensions = new AllDimensions(); {
            allDimensions.addDimension("tier").addValue("sit");
            allDimensions.addDimension("zone").addValue("uk");
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/dimension",
                        "test-property-with-single-value-with-multiple-dimensions"
                ),
                allDimensions
        );

        Assert.assertEquals(1, allProperties.getAllProperties().size());
        Assert.assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsPropertyInfo().size());

        Assert.assertEquals(1, allProperties.getAllProperties().get("a")
                .getDimensionsPropertyInfo()
                .stream()
                .filter(it -> it.getValue().equals(1))
                .filter(it -> it.getType().equals(PropertyType.NUMBER))
                .filter(it -> it.getDimensions().size() == 2)
                .filter(it -> it.getDimensions().contains("sit"))
                .filter(it -> it.getDimensions().contains("uk"))
                .count()
        );
    }

    @Test
    public void testPropertyWithMultipleValues() {
        AllDimensions allDimensions = new AllDimensions(); {
            Dimension tierDimension = allDimensions.addDimension("tier"); {
                tierDimension.addValue("sit");
                tierDimension.addValue("uat");
                tierDimension.addValue("prod");
            }
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/dimension",
                        "test-property-with-multiple-values"
                ),
                allDimensions
        );

        Assert.assertEquals(1, allProperties.getAllProperties().size());
        Assert.assertEquals(3, allProperties.getAllProperties().get("a").getDimensionsPropertyInfo().size());

        Assert.assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsPropertyInfo()
                        .stream()
                        .filter(it -> it.getValue().equals(1))
                        .filter(it -> it.getType().equals(PropertyType.NUMBER))
                        .filter(it -> it.getDimensions().size() == 1)
                        .filter(it -> it.getDimensions().contains("sit"))
                        .count()
        );

        Assert.assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsPropertyInfo()
                        .stream()
                        .filter(it -> it.getValue().equals(2))
                        .filter(it -> it.getType().equals(PropertyType.NUMBER))
                        .filter(it -> it.getDimensions().size() == 1)
                        .filter(it -> it.getDimensions().contains("uat"))
                        .count()
        );

        Assert.assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsPropertyInfo()
                        .stream()
                        .filter(it -> it.getValue().equals(3))
                        .filter(it -> it.getType().equals(PropertyType.NUMBER))
                        .filter(it -> it.getDimensions().size() == 1)
                        .filter(it -> it.getDimensions().contains("prod"))
                        .count()
        );
    }

    @Test(expected = RuntimeException.class)
    public void testPropertyWithUnrecognizedDimension() {
        AllDimensions allDimensions = new AllDimensions(); {
            Dimension dimension = allDimensions.addDimension("tier"); {
                dimension.addValue("sit");
            }
        }

        SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/dimension",
                        "test-property-with-unrecognized-dimension"
                ),
                allDimensions
        );
    }

    @Test(expected = RuntimeException.class)
    public void testPropertyWithSingleValueWithMultipleDimensionsWithConflict() {
        AllDimensions allDimensions = new AllDimensions(); {
            Dimension tierDimension = allDimensions.addDimension("tier"); {
                tierDimension.addValue("sit");
                tierDimension.addValue("uat");
            }
        }

        SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/dimension",
                        "test-property-with-single-value-with-multiple-dimensions-with-conflict"
                ),
                allDimensions
        );
    }
}
