package com.bdev.smart.config.parser;

import com.bdev.smart.config.data.inner.dimension.DimensionInfo;
import com.bdev.smart.config.reader.SmartConfigReader;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigValueType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class SmartConfigDimensionsParser {
    static Map<String, DimensionInfo> parse(String filePath) {
        Map<String, DimensionInfo> res = new HashMap<>();

        SmartConfigReader
                .read(filePath)
                .forEach(dimension -> {
                    if (res.containsKey(dimension.getKey())) {
                        throw new RuntimeException();
                    }

                    res.put(dimension.getKey(), new DimensionInfo());

                    if (dimension.getValue().valueType() != ConfigValueType.LIST) {
                        throw new RuntimeException();
                    }

                    Set<String> allDimensions = res.get(dimension.getKey()).getDimensions();

                    for (Object rawValue : ((ConfigList) dimension.getValue()).unwrapped()) {
                        String value = (String) rawValue;

                        if (allDimensions.contains(value)) {
                            throw new RuntimeException();
                        }

                        allDimensions.add(value);
                    }
                });

        long totalAmount = res.size();

        long distinctAmount = res.values()
                .stream()
                .distinct()
                .count();

        if (totalAmount != distinctAmount) {
            throw new RuntimeException();
        }

        return res;
    }
}
