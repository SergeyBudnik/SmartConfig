package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.DimensionInfo;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SmartConfigDimensionsParserTest extends SmartConfigParserTest {
    @Test
    public void testSingleDimensionSingleValue() {
        Map<String, DimensionInfo> dimensionsInfo = SmartConfigDimensionsParser.parse(
                getConfigPath(
                        "dimensions-parser",
                        "test-dimension-single-value-single"
                )
        );

        assertEquals(1, dimensionsInfo.size());
        assertEquals(1, dimensionsInfo.get("tier").getDimensions().size());
        assertTrue(dimensionsInfo.get("tier").getDimensions().contains("sit"));
    }

    @Test
    public void testSingleDimensionMultipleValues() {
        Map<String, DimensionInfo> dimensionsInfo = SmartConfigDimensionsParser.parse(
                getConfigPath(
                        "dimensions-parser",
                        "test-dimension-single-values-multiple"
                )
        );

        assertEquals(1, dimensionsInfo.size());
        assertEquals(2, dimensionsInfo.get("tier").getDimensions().size());
        assertTrue(dimensionsInfo.get("tier").getDimensions().contains("sit"));
        assertTrue(dimensionsInfo.get("tier").getDimensions().contains("uat"));
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
        Map<String, DimensionInfo> dimensionsInfo = SmartConfigDimensionsParser.parse(
                getConfigPath(
                        "dimensions-parser",
                        "test-dimensions-multiple-value-single"
                )
        );

        assertEquals(2, dimensionsInfo.size());

        assertEquals(1, dimensionsInfo.get("tier").getDimensions().size());
        assertTrue(dimensionsInfo.get("tier").getDimensions().contains("sit"));

        assertEquals(1, dimensionsInfo.get("zone").getDimensions().size());
        assertTrue(dimensionsInfo.get("zone").getDimensions().contains("uk"));
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
