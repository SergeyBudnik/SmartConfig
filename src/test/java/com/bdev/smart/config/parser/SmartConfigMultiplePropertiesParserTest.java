package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.AllDimensions;
import com.bdev.smart.config.data.inner.dimension.Dimension;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.data.inner.property.ConditionalProperty;
import com.bdev.smart.config.data.inner.property.Property;
import com.bdev.smart.config.data.inner.property.PropertyType;
import com.bdev.smart.config.data.util.Tuple;
import com.bdev.smart.config.parser.property.SmartConfigPropertiesParser;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SmartConfigMultiplePropertiesParserTest extends SmartConfigParserTest {
    @Test
    public void testSingleDimensionSingleValue() {
        AllDimensions allDimensions = new AllDimensions(); {
            Dimension tierDimension = allDimensions.addDimension("tier"); {
                tierDimension.addValue("sit");
                tierDimension.addValue("uat");
            }
        }

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/multiple-files",
                        "test-multiple-files-1",
                        "test-multiple-files-2"
                ),
                allDimensions
        );

        assertEquals(3, allProperties.getAllProperties().size());

        assertTrue(allProperties.getAllProperties().containsKey("a")); {
            {
                Property a = allProperties.getAllProperties().get("a");

                {
                    ConditionalProperty sitProperty = a
                            .getMostSuitableProperty(
                                    Stream.of(new Tuple<>("tier", "sit")).collect(Collectors.toList())
                            );

                    assertEquals(PropertyType.NUMBER, sitProperty.getType());
                    assertEquals(1, sitProperty.getValue());
                }

                {
                    ConditionalProperty uatProperty = a
                            .getMostSuitableProperty(
                                    Stream.of(new Tuple<>("tier", "uat")).collect(Collectors.toList())
                            );

                    assertEquals(PropertyType.NUMBER, uatProperty.getType());
                    assertEquals(8, uatProperty.getValue());
                }
            }

            {
                Property b1 = allProperties.getAllProperties().get("b1");

                {
                    ConditionalProperty sitProperty = b1
                            .getMostSuitableProperty(
                                    Stream.of(new Tuple<>("tier", "sit")).collect(Collectors.toList())
                            );

                    assertEquals(PropertyType.STRING, sitProperty.getType());
                    assertEquals("b", sitProperty.getValue());
                }

                {
                    ConditionalProperty uatProperty = b1
                            .getMostSuitableProperty(
                                    Stream.of(new Tuple<>("tier", "uat")).collect(Collectors.toList())
                            );

                    assertEquals(PropertyType.STRING, uatProperty.getType());
                    assertEquals("a", uatProperty.getValue());
                }
            }

            {
                Property b2 = allProperties.getAllProperties().get("b2");

                {
                    ConditionalProperty sitProperty = b2
                            .getMostSuitableProperty(
                                    Stream.of(new Tuple<>("tier", "sit")).collect(Collectors.toList())
                            );

                    assertEquals(PropertyType.STRING, sitProperty.getType());
                    assertEquals("c", sitProperty.getValue());
                }

                {
                    ConditionalProperty uatProperty = b2
                            .getMostSuitableProperty(
                                    Stream.of(new Tuple<>("tier", "uat")).collect(Collectors.toList())
                            );

                    assertEquals(PropertyType.STRING, uatProperty.getType());
                    assertEquals("d", uatProperty.getValue());
                }
            }
        }
    }
}
