package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.*;
import com.bdev.smart.config.parser.dimension.SmartConfigSpaceParser;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SmartConfigDimensionsParserTest extends SmartConfigParserTest {
    @Test
    public void testSingleDimensionSingleValue() {
        SpaceInfo spaceInfo = SmartConfigSpaceParser.parse(
                getConfigPath(
                        "dimensions-parser",
                        "test-dimension-single-value-single"
                )
        );

        Space space = spaceInfo.getSpace(); {
            assertEquals(1, space.getDimensions().size());

            Dimension sitDimension = space.getDimensionByName("tier")
                    .orElseThrow(RuntimeException::new); {
                assertEquals(1, sitDimension.getValues().size());
                assertTrue(sitDimension.getValue("sit").isPresent());
            }
        }

        assertEquals(1, spaceInfo.getPoints().size());
        assertTrue(spaceInfo.getPoints().stream()
                .anyMatch(it -> it.containsCoordinate("tier", "sit"))
        );
    }

    @Test
    public void testSingleDimensionMultipleValues() {
        SpaceInfo spaceInfo = SmartConfigSpaceParser.parse(
                getConfigPath(
                        "dimensions-parser",
                        "test-dimension-single-values-multiple"
                )
        );

        Space space = spaceInfo.getSpace(); {
            assertEquals(1, space.getDimensions().size());

            Dimension sitDimension = space.getDimensionByName("tier")
                    .orElseThrow(RuntimeException::new); {
                assertEquals(2, sitDimension.getValues().size());
                assertTrue(sitDimension.getValue("sit").isPresent());
                assertTrue(sitDimension.getValue("uat").isPresent());
            }
        }

        Set<Point> points = spaceInfo.getPoints(); {
            assertEquals(2, points.size());

            assertTrue(points.stream()
                    .anyMatch(it -> it.containsCoordinate("tier", "sit"))
            );

            assertTrue(points.stream()
                    .anyMatch(it -> it.containsCoordinate("tier", "uat"))
            );
        }
    }

    @Test
    public void testMultipleDimensionsSingleValue() {
        SpaceInfo spaceInfo = SmartConfigSpaceParser.parse(
                getConfigPath(
                        "dimensions-parser",
                        "test-dimensions-multiple-value-single"
                )
        );

        Space space = spaceInfo.getSpace(); {
            assertEquals(2, space.getDimensions().size());

            Dimension tierDimension = space.getDimensionByName("tier")
                    .orElseThrow(RuntimeException::new); {
                assertEquals(1, tierDimension.getValues().size());
                assertTrue(tierDimension.getValue("sit").isPresent());
            }

            Dimension zoneDimension = space.getDimensionByName("zone")
                    .orElseThrow(RuntimeException::new); {
                assertEquals(1, zoneDimension.getValues().size());
                assertTrue(zoneDimension.getValue("uk").isPresent());
            }
        }

        Set<Point> points = spaceInfo.getPoints(); {
            assertEquals(1, points.size());

            assertTrue(points.stream()
                    .filter(it -> it.containsCoordinate("tier", "sit"))
                    .filter(it -> it.containsCoordinate("zone", "uk"))
                    .anyMatch(it -> true)
            );
        }
    }

    @Test(expected = RuntimeException.class)
    public void testMultipleDimensionsSingleValueValuesConflict() {
        try {
            SmartConfigSpaceParser.parse(
                    getConfigPath(
                            "dimensions-parser",
                            "test-dimensions-multiple-value-single-values-conflict"
                    )
            );
        } catch (RuntimeException e) {
            assertEquals(
                    "Dimension 'tier' already contains value 'sit'",
                    e.getMessage()
            );

            throw e;
        }
    }

    @Test(expected = RuntimeException.class)
    public void testSingleDimensionMultipleValuesConflict() {
        try {
            SmartConfigSpaceParser.parse(
                    getConfigPath(
                            "dimensions-parser",
                            "test-dimension-single-values-multiple-conflict"
                    )
            );
        } catch (RuntimeException e) {
            assertEquals(
                    "Dimension 'tier' already contains value 'sit'",
                    e.getMessage()
            );

            throw e;
        }
    }

    @Test(expected = RuntimeException.class)
    public void testDimensionUncompleted() {
        try {
            SmartConfigSpaceParser.parse(
                    getConfigPath(
                            "dimensions-parser",
                            "test-dimension-uncompleted"
                    )
            );
        } catch (RuntimeException e) {
            assertEquals(
                    "Point \n{\ntier: sit\n}\nis not completed\n",
                    e.getMessage()
            );

            throw e;
        }
    }
}
