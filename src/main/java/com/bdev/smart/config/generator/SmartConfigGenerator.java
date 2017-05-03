package com.bdev.smart.config.generator;

import com.bdev.smart.config.parser.SmartConfigDimensionsParser;
import net.sourceforge.jenesis4java.VirtualMachine;
import net.sourceforge.jenesis4java.jaloppy.JenesisJalopyEncoder;

import java.util.Map;
import java.util.Set;

public class SmartConfigGenerator {
    public static void generate(
            String rootPath,
            String propertiesPath,
            String dimensionsPath
    ) throws Exception {
        System.setProperty("jenesis.encoder", JenesisJalopyEncoder.class.getName());

        VirtualMachine vm = VirtualMachine.getVirtualMachine();

        //Map<String, Set<String>> dimensions = SmartConfigDimensionsParser.parse(propertiesPath, dimensionsPath);

        //SmartConfigDimensionsGenerator.generate(vm, rootPath, dimensions);
    }
}
