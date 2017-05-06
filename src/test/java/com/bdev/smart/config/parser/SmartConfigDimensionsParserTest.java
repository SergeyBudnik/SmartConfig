package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.DimensionInfo;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SmartConfigDimensionsParserTest {
    @Test
    public void testSingleDimensionSingleValue() {
        Map<String, DimensionInfo> dimensionsInfo = SmartConfigDimensionsParser.parse(
                new File("src/test/resources/dimensions-parser/test-dimension-single-value-single.conf").getPath()
        );

        assertEquals(1, dimensionsInfo.size());
        assertEquals(1, dimensionsInfo.get("tier").getDimensions().size());
        assertTrue(dimensionsInfo.get("tier").getDimensions().contains("sit"));
    }

    @Test
    public void testSingleDimensionMultipleValues() {
        Map<String, DimensionInfo> dimensionsInfo = SmartConfigDimensionsParser.parse(
                new File("src/test/resources/dimensions-parser/test-dimension-single-values-multiple.conf").getPath()
        );

        assertEquals(1, dimensionsInfo.size());
        assertEquals(2, dimensionsInfo.get("tier").getDimensions().size());
        assertTrue(dimensionsInfo.get("tier").getDimensions().contains("sit"));
        assertTrue(dimensionsInfo.get("tier").getDimensions().contains("uat"));
    }

    @Test(expected = RuntimeException.class)
    public void testSingleDimensionMultipleValuesConflict() {
        SmartConfigDimensionsParser.parse(
                new File("src/test/resources/dimensions-parser/test-dimension-single-values-multiple-conflict.conf").getPath()
        );
    }

    @Test
    public void testMultipleDimensionsSingleValue() {
        Map<String, DimensionInfo> dimensionsInfo = SmartConfigDimensionsParser.parse(
                new File("src/test/resources/dimensions-parser/test-dimensions-multiple-value-single.conf").getPath()
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
                new File("src/test/resources/dimensions-parser/test-dimensions-multiple-value-single-names-conflict.conf").getPath()
        );
    }

    @Test(expected = RuntimeException.class)
    public void testMultipleDimensionsSingleValueValuesConflict() {
        SmartConfigDimensionsParser.parse(
                new File("src/test/resources/dimensions-parser/test-dimensions-multiple-value-single-values-conflict.conf").getPath()
        );
    }
}
