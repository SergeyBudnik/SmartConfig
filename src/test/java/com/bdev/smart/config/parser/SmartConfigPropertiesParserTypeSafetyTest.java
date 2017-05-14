package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.AllDimensions;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.data.inner.property.Property;
import com.bdev.smart.config.data.inner.property.PropertyType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class SmartConfigPropertiesParserTypeSafetyTest extends SmartConfigParserTest {
    @Test
    public void testSimpleInteger() {
        AllDimensions allDimensions = new AllDimensions(); {
            allDimensions.addDimension("tier").addValue("sit");
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/type-safety",
                        "test-type-safety-integer"
                ),
                allDimensions
        );

        Assert.assertEquals(1, allProperties.getAllProperties().size());
        Assert.assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsPropertyInfo().size());

        Assert.assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsPropertyInfo()
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
        AllDimensions allDimensions = new AllDimensions(); {
            allDimensions.addDimension("tier").addValue("sit");
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/type-safety",
                        "test-type-safety-string"
                ),
                allDimensions
        );

        Assert.assertEquals(1, allProperties.getAllProperties().size());
        Assert.assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsPropertyInfo().size());

        Assert.assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsPropertyInfo()
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
        AllDimensions allDimensions = new AllDimensions(); {
            allDimensions.addDimension("tier").addValue("sit");
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/type-safety",
                        "test-type-safety-boolean"
                ),
                allDimensions
        );

        Assert.assertEquals(1, allProperties.getAllProperties().size());
        Assert.assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsPropertyInfo().size());

        Assert.assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsPropertyInfo()
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
        AllDimensions allDimensions = new AllDimensions(); {
            allDimensions.addDimension("tier").addValue("sit");
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/type-safety",
                        "test-type-safety-list-of-strings"
                ),
                allDimensions
        );

        Assert.assertEquals(1, allProperties.getAllProperties().size());
        Assert.assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsPropertyInfo().size());

        Assert.assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsPropertyInfo()
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
        AllDimensions allDimensions = new AllDimensions(); {
            allDimensions.addDimension("tier").addValue("sit");
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/type-safety",
                        "test-type-safety-list-of-numbers"
                ),
                allDimensions
        );

        Assert.assertEquals(1, allProperties.getAllProperties().size());
        Assert.assertEquals(1, allProperties.getAllProperties().get("a").getDimensionsPropertyInfo().size());

        Assert.assertEquals(1, allProperties.getAllProperties().get("a")
                        .getDimensionsPropertyInfo()
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
        AllDimensions allDimensions = new AllDimensions(); {
            allDimensions.addDimension("tier").addValue("sit");
        }

        SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/type-safety",
                        "test-type-safety-list-of-mixed"
                ),
                allDimensions
        );
    }
}
