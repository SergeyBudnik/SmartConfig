package com.bdev.smart.config.generator;

import net.sourceforge.jenesis4java.VirtualMachine;
import net.sourceforge.jenesis4java.jaloppy.JenesisJalopyEncoder;

public class SmartConfigGenerator {
    public static void generate(
            String rootPath,
            String propertiesPath,
            String dimensionsPath
    ) throws Exception {
        System.setProperty("jenesis.encoder", JenesisJalopyEncoder.class.getName());

        VirtualMachine vm = VirtualMachine.getVirtualMachine();

        SmartConfigDimensionsGenerator.generate(vm, rootPath, dimensionsPath);
    }
}
