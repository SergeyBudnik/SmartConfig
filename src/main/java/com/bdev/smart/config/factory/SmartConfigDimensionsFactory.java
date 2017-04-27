package com.bdev.smart.config.factory;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigList;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SmartConfigDimensionsFactory {
    public static Map<String, Set<String>> produce(String filePath) {
        File file = new File(filePath);

        // todo: check if file exists

        Map<String, Set<String>> res = new HashMap<>();

        ConfigFactory
                .parseFile(file)
                .entrySet()
                .forEach(dimension -> {
                    // ToDo: validation

                    res.put(dimension.getKey(), new HashSet<>());

                    for (Object value : ((ConfigList) dimension.getValue()).unwrapped()) {
                        res.get(dimension.getKey()).add((String) value);
                    }
                });

        return res;
    }
}
