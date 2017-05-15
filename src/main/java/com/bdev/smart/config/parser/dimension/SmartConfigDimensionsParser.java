package com.bdev.smart.config.parser.dimension;

import com.bdev.smart.config.data.inner.dimension.AllDimensions;
import com.bdev.smart.config.data.inner.dimension.Dimension;
import com.bdev.smart.config.reader.SmartConfigReader;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigValueType;

public class SmartConfigDimensionsParser {
    public static AllDimensions parse(String filePath) {
        AllDimensions allDimensions = new AllDimensions();

        SmartConfigReader
                .read(filePath)
                .forEach(configDimension -> {
                    if (configDimension.getValue().valueType() != ConfigValueType.LIST) {
                        throw new RuntimeException();
                    }

                    Dimension dimension = allDimensions.addDimension(configDimension.getKey());

                    for (Object dimensionValue : ((ConfigList) configDimension.getValue()).unwrapped()) {
                        dimension.addValue((String) dimensionValue);
                    }
                });

        return allDimensions;
    }
}
