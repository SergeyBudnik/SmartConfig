package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.DimensionInfo;
import com.bdev.smart.config.data.inner.PropertyInfo;
import com.bdev.smart.config.data.inner.PropertyType;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SmartConfigPropertiesParserTest_Dimensions extends SmartConfigParserTest {
    @Test
    public void testSingleDimensionForSingleValue() {
        Map<String, DimensionInfo> dimensions = new HashMap<>(); {
            dimensions.put("tier", new DimensionInfo()); {
                dimensions.get("tier").getDimensions().add("sit");
            }
        }

        Map<String, PropertyInfo> propertiesInfo = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/dimension",
                        "test-dimension-single-value-single"
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
    public void testSingleDimensionForMultipleValues() {
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
                        "test-dimension-single-values-multiple"
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
    public void testUnrecognizedDimension() {
        Map<String, DimensionInfo> dimensions = new HashMap<>(); {
            dimensions.put("tier", new DimensionInfo()); {
                dimensions.get("tier").getDimensions().add("sit");
            }
        }

        SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/dimension",
                        "test-dimension-unrecognized"
                ),
                dimensions
        );
    }

    @Test(expected = RuntimeException.class)
    public void testSingleDimensionForSingleValueConflict() {
        Map<String, DimensionInfo> dimensions = new HashMap<>(); {
            dimensions.put("tier", new DimensionInfo()); {
                dimensions.get("tier").getDimensions().add("sit");
                dimensions.get("tier").getDimensions().add("uat");
            }
        }

        SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/dimension",
                        "test-dimension-single-value-single-conflict"
                ),
                dimensions
        );
    }

    /**
     * ToDo: to be fixed, probably should be moved to a separate test class with multiple tests.
     */
    @Test(expected = RuntimeException.class)
    @Ignore
    public void testMultipleDimensionMultipleValuesConflict() {
        Map<String, DimensionInfo> dimensions = new HashMap<>(); {
            dimensions.put("tier", new DimensionInfo()); {
                dimensions.get("tier").getDimensions().add("sit");
            }

            dimensions.put("zone", new DimensionInfo()); {
                dimensions.get("zone").getDimensions().add("uk");
            }
        }

        SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/dimension",
                        "test-dimensions-multiple-values-multiple-conflict"
                ),
                dimensions
        );
    }
}
