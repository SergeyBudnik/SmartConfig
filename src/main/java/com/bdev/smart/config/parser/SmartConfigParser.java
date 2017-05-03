package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.DimensionInfo;
import com.bdev.smart.config.data.inner.PropertyInfo;

import java.util.Map;

public class SmartConfigParser {
    public static ConfigInfo parse(String dimensionsPath, String propertiesPath) {
        Map<String, DimensionInfo> dimensions = SmartConfigDimensionsParser.parse(dimensionsPath);
        Map<String, PropertyInfo> properties = SmartConfigPropertiesParser.parse(propertiesPath, dimensions);

        return new ConfigInfo(dimensions, properties);
    }
}
