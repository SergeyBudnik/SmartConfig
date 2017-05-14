package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.AllDimensions;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SmartConfigDimensionsParserTest extends SmartConfigParserTest {
    @Test
    public void testSingleDimensionSingleValue() {
        AllDimensions allDimensions = SmartConfigDimensionsParser.parse(
                getConfigPath(
                        "dimensions-parser",
                        "test-dimension-single-value-single"
                )
        );

        assertEquals(1, allDimensions.getDimensions().size());
        assertEquals(1, allDimensions.getDimensions().get("tier").getValues().size());
        assertTrue(allDimensions.getDimensions().get("tier").getValues().contains("sit"));
    }

    @Test
    public void testSingleDimensionMultipleValues() {
        AllDimensions dimensionsInfo = SmartConfigDimensionsParser.parse(
                getConfigPath(
                        "dimensions-parser",
                        "test-dimension-single-values-multiple"
                )
        );

        assertEquals(1, dimensionsInfo.getDimensions().size());
        assertEquals(2, dimensionsInfo.getDimensions().get("tier").getValues().size());
        assertTrue(dimensionsInfo.getDimensions().get("tier").getValues().contains("sit"));
        assertTrue(dimensionsInfo.getDimensions().get("tier").getValues().contains("uat"));
    }

    @Test(expected = RuntimeException.class)
    public void testSingleDimensionMultipleValuesConflict() {
        SmartConfigDimensionsParser.parse(
                getConfigPath(
                        "dimensions-parser",
                        "test-dimension-single-values-multiple-conflict"
                )
        );
    }

    @Test
    public void testMultipleDimensionsSingleValue() {
        AllDimensions dimensionsInfo = SmartConfigDimensionsParser.parse(
                getConfigPath(
                        "dimensions-parser",
                        "test-dimensions-multiple-value-single"
                )
        );

        assertEquals(2, dimensionsInfo.getDimensions().size());

        assertEquals(1, dimensionsInfo.getDimensions().get("tier").getValues().size());
        assertTrue(dimensionsInfo.getDimensions().get("tier").getValues().contains("sit"));

        assertEquals(1, dimensionsInfo.getDimensions().get("zone").getValues().size());
        assertTrue(dimensionsInfo.getDimensions().get("zone").getValues().contains("uk"));
    }

    /**
     * Equal keys are ignored by typesafe config implementation
     */
    @Test(expected = RuntimeException.class)
    @Ignore
    public void testMultipleDimensionsSingleValueNamesConflict() {
        SmartConfigDimensionsParser.parse(
                getConfigPath(
                        "dimensions-parser",
                        "test-dimensions-multiple-value-single-names-conflict"
                )
        );
    }

    @Test(expected = RuntimeException.class)
    public void testMultipleDimensionsSingleValueValuesConflict() {
        SmartConfigDimensionsParser.parse(
                getConfigPath(
                        "dimensions-parser",
                        "test-dimensions-multiple-value-single-values-conflict"
                )
        );
    }
}
