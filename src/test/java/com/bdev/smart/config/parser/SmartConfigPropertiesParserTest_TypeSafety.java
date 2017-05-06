package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.DimensionInfo;
import com.bdev.smart.config.data.inner.PropertyInfo;
import com.bdev.smart.config.data.inner.PropertyType;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartConfigPropertiesParserTest_TypeSafety extends SmartConfigParserTest {
    @Test
    public void testSimpleInteger() {
        Map<String, DimensionInfo> dimensions = new HashMap<>(); {
            dimensions.put("tier", new DimensionInfo()); {
                dimensions.get("tier").getDimensions().add("sit");
            }
        }

        Map<String, PropertyInfo> propertiesInfo = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/type-safety",
                        "test-type-safety-integer"
                ),
                dimensions
        );

        Assert.assertEquals(1, propertiesInfo.size());
        Assert.assertEquals(1, propertiesInfo.get("a").getDimensionPropertyInfo().size());

        Assert.assertEquals(1, propertiesInfo.get("a")
                        .getDimensionPropertyInfo()
                        .stream()
                        .filter(it -> it.getValue().equals(3))
                        .filter(it -> it.getType().equals(PropertyType.NUMBER))
                        .filter(it -> it.getDimensions().size() == 1)
                        .filter(it -> it.getDimensions().contains("sit"))
                        .count()
        );
    }

    @Test
    public void testSimpleString() {
        Map<String, DimensionInfo> dimensions = new HashMap<>(); {
            dimensions.put("tier", new DimensionInfo()); {
                dimensions.get("tier").getDimensions().add("sit");
            }
        }

        Map<String, PropertyInfo> propertiesInfo = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/type-safety",
                        "test-type-safety-string"
                ),
                dimensions
        );

        Assert.assertEquals(1, propertiesInfo.size());
        Assert.assertEquals(1, propertiesInfo.get("a").getDimensionPropertyInfo().size());

        Assert.assertEquals(1, propertiesInfo.get("a")
                        .getDimensionPropertyInfo()
                        .stream()
                        .filter(it -> it.getValue().equals("hello"))
                        .filter(it -> it.getType().equals(PropertyType.STRING))
                        .filter(it -> it.getDimensions().size() == 1)
                        .filter(it -> it.getDimensions().contains("sit"))
                        .count()
        );
    }

    @Test
    public void testSimpleBoolean() {
        Map<String, DimensionInfo> dimensions = new HashMap<>(); {
            dimensions.put("tier", new DimensionInfo()); {
                dimensions.get("tier").getDimensions().add("sit");
            }
        }

        Map<String, PropertyInfo> propertiesInfo = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/type-safety",
                        "test-type-safety-boolean"
                ),
                dimensions
        );

        Assert.assertEquals(1, propertiesInfo.size());
        Assert.assertEquals(1, propertiesInfo.get("a").getDimensionPropertyInfo().size());

        Assert.assertEquals(1, propertiesInfo.get("a")
                        .getDimensionPropertyInfo()
                        .stream()
                        .filter(it -> it.getValue().equals(false))
                        .filter(it -> it.getType().equals(PropertyType.BOOLEAN))
                        .filter(it -> it.getDimensions().size() == 1)
                        .filter(it -> it.getDimensions().contains("sit"))
                        .count()
        );
    }

    @Test
    public void testSimpleListOfStrings() {
        Map<String, DimensionInfo> dimensions = new HashMap<>(); {
            dimensions.put("tier", new DimensionInfo()); {
                dimensions.get("tier").getDimensions().add("sit");
            }
        }

        Map<String, PropertyInfo> propertiesInfo = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/type-safety",
                        "test-type-safety-list-of-strings"
                ),
                dimensions
        );

        Assert.assertEquals(1, propertiesInfo.size());
        Assert.assertEquals(1, propertiesInfo.get("a").getDimensionPropertyInfo().size());

        Assert.assertEquals(1, propertiesInfo.get("a")
                        .getDimensionPropertyInfo()
                        .stream()
                        .filter(it -> ((List) it.getValue()).size() == 3)
                        .filter(it -> ((List) it.getValue()).contains("hello_1"))
                        .filter(it -> ((List) it.getValue()).contains("hello_2"))
                        .filter(it -> ((List) it.getValue()).contains("hello_3"))
                        .filter(it -> it.getType().equals(PropertyType.LIST_OF_STRINGS))
                        .filter(it -> it.getDimensions().size() == 1)
                        .filter(it -> it.getDimensions().contains("sit"))
                        .count()
        );
    }

    @Test
    public void testSimpleListOfNumbers() {
        Map<String, DimensionInfo> dimensions = new HashMap<>(); {
            dimensions.put("tier", new DimensionInfo()); {
                dimensions.get("tier").getDimensions().add("sit");
            }
        }

        Map<String, PropertyInfo> propertiesInfo = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/type-safety",
                        "test-type-safety-list-of-numbers"
                ),
                dimensions
        );

        Assert.assertEquals(1, propertiesInfo.size());
        Assert.assertEquals(1, propertiesInfo.get("a").getDimensionPropertyInfo().size());

        Assert.assertEquals(1, propertiesInfo.get("a")
                        .getDimensionPropertyInfo()
                        .stream()
                        .filter(it -> ((List) it.getValue()).size() == 3)
                        .filter(it -> ((List) it.getValue()).contains(1))
                        .filter(it -> ((List) it.getValue()).contains(2))
                        .filter(it -> ((List) it.getValue()).contains(3))
                        .filter(it -> it.getType().equals(PropertyType.LIST_OF_NUMBERS))
                        .filter(it -> it.getDimensions().size() == 1)
                        .filter(it -> it.getDimensions().contains("sit"))
                        .count()
        );
    }

    @Test(expected = RuntimeException.class)
    public void testSimpleListOfMixed() {
        Map<String, DimensionInfo> dimensions = new HashMap<>(); {
            dimensions.put("tier", new DimensionInfo()); {
                dimensions.get("tier").getDimensions().add("sit");
            }
        }

        SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/type-safety",
                        "test-type-safety-list-of-mixed"
                ),
                dimensions
        );
    }
}
