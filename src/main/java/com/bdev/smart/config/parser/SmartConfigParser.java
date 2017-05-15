package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.dimension.AllDimensions;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.parser.dimension.SmartConfigDimensionsParser;
import com.bdev.smart.config.parser.property.SmartConfigPropertiesParser;

public class SmartConfigParser {
    public static ConfigInfo parse(String dimensionsPath, String propertiesPath) {
        AllDimensions allDimensions = SmartConfigDimensionsParser.parse(dimensionsPath);
        AllProperties allProperties = SmartConfigPropertiesParser.parse(propertiesPath, allDimensions);

        return new ConfigInfo(allDimensions, allProperties);
    }
}
