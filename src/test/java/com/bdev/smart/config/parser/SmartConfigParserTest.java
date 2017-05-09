package com.bdev.smart.config.parser;

import java.io.File;

abstract class SmartConfigParserTest {
    String getConfigPath(String folder, String file) {
        return new File("src/test/resources/" + folder + "/" + file + ".conf").getPath();
    }
}
