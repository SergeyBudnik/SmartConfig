package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.dimension.SpaceInfo;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.parser.dimension.SmartConfigSpaceParser;
import com.bdev.smart.config.parser.property.SmartConfigPropertiesParser;

public class SmartConfigParser {
    public static ConfigInfo parse(String dimensionsPath, String csvPropertiesPath) {
        SpaceInfo spaceInfo = SmartConfigSpaceParser.parse(dimensionsPath);
        AllProperties allProperties = SmartConfigPropertiesParser.parse(csvPropertiesPath, spaceInfo);

        return new ConfigInfo(spaceInfo, allProperties);
    }
}
