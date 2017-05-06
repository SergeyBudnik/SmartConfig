package com.bdev.smart.config.parser;

import java.io.File;

public abstract class SmartConfigParserTest {
    protected String getConfigPath(String folder, String file) {
        return new File("src/test/resources/" + folder + "/" + file + ".conf").getPath();
    }
}
