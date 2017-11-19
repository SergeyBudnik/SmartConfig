package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.*;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.data.inner.property.ConditionalProperty;
import com.bdev.smart.config.data.inner.property.Property;
import com.bdev.smart.config.data.inner.property.PropertyType;
import com.bdev.smart.config.parser.property.SmartConfigPropertiesParser;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SmartConfigMultiplePropertiesParserTest extends SmartConfigParserTest {
    @Test
    public void testSingleDimensionSingleValue() {
        Space space = new Space(); {
            Dimension tierDimension = new Dimension("tier"); {
                tierDimension.addValue(new DimensionValue("sit"));
                tierDimension.addValue(new DimensionValue("uat"));
            }
            
            space.addDimension(tierDimension);
        }

        Point sitPoint = new Point(); {
            sitPoint.addCoordinate(new Dimension("tier"), new DimensionValue("sit"));
        }

        Point uatPoint = new Point(); {
            uatPoint.addCoordinate(new Dimension("tier"), new DimensionValue("uat"));
        }

        Set<Point> points = new HashSet<>(); {
            points.add(sitPoint);
            points.add(uatPoint);
        }
        
        SpaceInfo spaceInfo = new SpaceInfo(space, points);

        AllProperties allProperties = SmartConfigPropertiesParser.parse(
                getConfigPath(
                        "properties-parser/multiple-files",
                        "test-multiple-files-1",
                        "test-multiple-files-2"
                ),
                spaceInfo
        );

        assertEquals(3, allProperties.getAllProperties().size());

        assertTrue(allProperties.getAllProperties().containsKey("a")); {
            {
                Property a = allProperties.getAllProperties().get("a");

                {
                    ConditionalProperty sitProperty = a.getMostSuitableProperty(sitPoint);

                    assertEquals(PropertyType.NUMBER, sitProperty.getType());
                    assertEquals(1, sitProperty.getValue());
                }

                {
                    ConditionalProperty uatProperty = a.getMostSuitableProperty(uatPoint);

                    assertEquals(PropertyType.NUMBER, uatProperty.getType());
                    assertEquals(8, uatProperty.getValue());
                }
            }

            {
                Property b1 = allProperties.getAllProperties().get("b1");

                {
                    ConditionalProperty sitProperty = b1.getMostSuitableProperty(sitPoint);

                    assertEquals(PropertyType.STRING, sitProperty.getType());
                    assertEquals("b", sitProperty.getValue());
                }

                {
                    ConditionalProperty uatProperty = b1.getMostSuitableProperty(uatPoint);

                    assertEquals(PropertyType.STRING, uatProperty.getType());
                    assertEquals("a", uatProperty.getValue());
                }
            }

            {
                Property b2 = allProperties.getAllProperties().get("b2");

                {
                    ConditionalProperty sitProperty = b2.getMostSuitableProperty(sitPoint);

                    assertEquals(PropertyType.STRING, sitProperty.getType());
                    assertEquals("c", sitProperty.getValue());
                }

                {
                    ConditionalProperty uatProperty = b2.getMostSuitableProperty(uatPoint);

                    assertEquals(PropertyType.STRING, uatProperty.getType());
                    assertEquals("d", uatProperty.getValue());
                }
            }
        }
    }
}
