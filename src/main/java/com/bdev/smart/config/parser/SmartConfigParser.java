package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.dimension.AllDimensions;
import com.bdev.smart.config.data.inner.dimension.Dimension;
import com.bdev.smart.config.data.inner.property.PropertyInfo;

import java.util.Map;

public class SmartConfigParser {
    public static ConfigInfo parse(String dimensionsPath, String propertiesPath) {
        AllDimensions allDimensions = SmartConfigDimensionsParser.parse(dimensionsPath);
        Map<String, PropertyInfo> properties = SmartConfigPropertiesParser.parse(propertiesPath, allDimensions);

        return new ConfigInfo(allDimensions, properties);
    }
}
