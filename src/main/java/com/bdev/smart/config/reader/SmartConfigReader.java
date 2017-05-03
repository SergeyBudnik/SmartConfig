package com.bdev.smart.config.reader;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

import java.io.File;
import java.util.Map;
import java.util.Set;

public class SmartConfigReader {
    public static Set<Map.Entry<String, ConfigValue>> read(String path) {
        return ConfigFactory
                .parseFile(new File(path))
                .entrySet();
    }
}
