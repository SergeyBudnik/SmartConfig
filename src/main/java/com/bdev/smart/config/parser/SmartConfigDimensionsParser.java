package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.DimensionInfo;
import com.bdev.smart.config.reader.SmartConfigReader;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigList;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SmartConfigDimensionsParser {
    public static Map<String, DimensionInfo> parse(String filePath) {
        Map<String, DimensionInfo> res = new HashMap<>();

        SmartConfigReader
                .read(filePath)
                .forEach(dimension -> {
                    // ToDo: validation

                    res.put(dimension.getKey(), new DimensionInfo());

                    for (Object value : ((ConfigList) dimension.getValue()).unwrapped()) {
                        res.get(dimension.getKey()).getDimensions().add((String) value);
                    }
                });

        return res;
    }
}
