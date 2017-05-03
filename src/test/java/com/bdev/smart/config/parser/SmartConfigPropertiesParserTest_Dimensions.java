package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.DimensionInfo;
import com.bdev.smart.config.data.inner.PropertyInfo;
import com.bdev.smart.config.data.inner.PropertyType;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SmartConfigPropertiesParserTest_Dimensions {
    @Test
    public void testSingleDimensionForSingleValue() {
        Map<String, DimensionInfo> dimensions = new HashMap<>(); {
            dimensions.put("tier", new DimensionInfo()); {
                dimensions.get("tier").getDimensions().add("sit");
            }
        }

        Map<String, PropertyInfo> propertiesInfo = SmartConfigPropertiesParser.parse(
                new File("src/test/resources/properties-parser/dimension/test-dimension-single-value-single.conf").getPath(),
                dimensions
        );

        Assert.assertEquals(1, propertiesInfo.size());
        Assert.assertEquals(1, propertiesInfo.get("a").getDimensionPropertyInfo().size());

        Assert.assertEquals(1, propertiesInfo.get("a")
                        .getDimensionPropertyInfo()
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
                new File("src/test/resources/properties-parser/dimension/test-dimension-single-values-multiple.conf").getPath(),
                dimensions
        );

        Assert.assertEquals(1, propertiesInfo.size());
        Assert.assertEquals(3, propertiesInfo.get("a").getDimensionPropertyInfo().size());

        Assert.assertEquals(1, propertiesInfo.get("a")
                        .getDimensionPropertyInfo()
                        .stream()
                        .filter(it -> it.getValue().equals(1))
                        .filter(it -> it.getType().equals(PropertyType.NUMBER))
                        .filter(it -> it.getDimensions().size() == 1)
                        .filter(it -> it.getDimensions().contains("sit"))
                        .count()
        );

        Assert.assertEquals(1, propertiesInfo.get("a")
                        .getDimensionPropertyInfo()
                        .stream()
                        .filter(it -> it.getValue().equals(2))
                        .filter(it -> it.getType().equals(PropertyType.NUMBER))
                        .filter(it -> it.getDimensions().size() == 1)
                        .filter(it -> it.getDimensions().contains("uat"))
                        .count()
        );

        Assert.assertEquals(1, propertiesInfo.get("a")
                        .getDimensionPropertyInfo()
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
                new File("src/test/resources/properties-parser/dimension/test-dimension-unrecognized.conf").getPath(),
                dimensions
        );
    }
}
