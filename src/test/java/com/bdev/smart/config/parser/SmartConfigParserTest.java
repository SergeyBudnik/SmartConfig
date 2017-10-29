package com.bdev.smart.config.parser;

import java.io.File;

abstract class SmartConfigParserTest {
    String getConfigPath(String folder, String... files) {
        StringBuilder pathBuilder = new StringBuilder();

        for (String file : files) {
            String path = new File("src/test/resources/" + folder + "/" + file + ".conf").getPath();

            if (pathBuilder.length() != 0) {
                pathBuilder.append(",");
            }

            pathBuilder.append(path);
        }

        return pathBuilder.toString();
    }
}
