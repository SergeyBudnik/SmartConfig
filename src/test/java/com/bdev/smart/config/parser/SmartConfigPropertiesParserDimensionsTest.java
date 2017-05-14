package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.DimensionInfo;
import com.bdev.smart.config.data.inner.PropertyInfo;
import com.bdev.smart.config.data.inner.PropertyType;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SmartConfigPropertiesParserDimensionsTest extends SmartConfigParserTest {
    @Test
    public void testPropertyWithSingleValueWithSingleDimension() {
        Map<String, DimensionInfo> dimensions = new HashMap<>(); {
            dimensions.put("tier", new DimensionInfo()); {
                dimensions.get("tier").getDimensions().add("sit");
            }
        }

        Map<String, PropertyInfo> propertiesInfo = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/dimension",
                        "test-property-with-single-value-with-single-dimension"
                ),
                dimensions
        );

        Assert.assertEquals(1, propertiesInfo.size());
        Assert.assertEquals(1, propertiesInfo.get("a").getDimensionsPropertyInfo().size());

        Assert.assertEquals(1, propertiesInfo.get("a")
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
        Map<String, DimensionInfo> dimensions = new HashMap<>(); {
            dimensions.put("tier", new DimensionInfo()); {
                dimensions.get("tier").getDimensions().add("sit");
            }

            dimensions.put("zone", new DimensionInfo()); {
                dimensions.get("zone").getDimensions().add("uk");
            }
        }

        Map<String, PropertyInfo> propertiesInfo = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/dimension",
                        "test-property-with-single-value-with-multiple-dimensions"
                ),
                dimensions
        );

        Assert.assertEquals(1, propertiesInfo.size());
        Assert.assertEquals(1, propertiesInfo.get("a").getDimensionsPropertyInfo().size());

        Assert.assertEquals(1, propertiesInfo.get("a")
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
        Map<String, DimensionInfo> dimensions = new HashMap<>(); {
            dimensions.put("tier", new DimensionInfo()); {
                dimensions.get("tier").getDimensions().add("sit");
                dimensions.get("tier").getDimensions().add("uat");
                dimensions.get("tier").getDimensions().add("prod");
            }
        }

        Map<String, PropertyInfo> propertiesInfo = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/dimension",
                        "test-property-with-multiple-values"
                ),
                dimensions
        );

        Assert.assertEquals(1, propertiesInfo.size());
        Assert.assertEquals(3, propertiesInfo.get("a").getDimensionsPropertyInfo().size());

        Assert.assertEquals(1, propertiesInfo.get("a")
                        .getDimensionsPropertyInfo()
                        .stream()
                        .filter(it -> it.getValue().equals(1))
                        .filter(it -> it.getType().equals(PropertyType.NUMBER))
                        .filter(it -> it.getDimensions().size() == 1)
                        .filter(it -> it.getDimensions().contains("sit"))
                        .count()
        );

        Assert.assertEquals(1, propertiesInfo.get("a")
                        .getDimensionsPropertyInfo()
                        .stream()
                        .filter(it -> it.getValue().equals(2))
                        .filter(it -> it.getType().equals(PropertyType.NUMBER))
                        .filter(it -> it.getDimensions().size() == 1)
                        .filter(it -> it.getDimensions().contains("uat"))
                        .count()
        );

        Assert.assertEquals(1, propertiesInfo.get("a")
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
        Map<String, DimensionInfo> dimensions = new HashMap<>(); {
            dimensions.put("tier", new DimensionInfo()); {
                dimensions.get("tier").getDimensions().add("sit");
            }
        }

        SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/dimension",
                        "test-property-with-unrecognized-dimension"
                ),
                dimensions
        );
    }

    @Test(expected = RuntimeException.class)
    public void testPropertyWithSingleValueWithMultipleDimensionsWithConflict() {
        Map<String, DimensionInfo> dimensions = new HashMap<>(); {
            dimensions.put("tier", new DimensionInfo()); {
                dimensions.get("tier").getDimensions().add("sit");
                dimensions.get("tier").getDimensions().add("uat");
            }
        }

        SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/dimension",
                        "test-property-with-single-value-with-multiple-dimensions-with-conflict"
                ),
                dimensions
        );
    }
}
