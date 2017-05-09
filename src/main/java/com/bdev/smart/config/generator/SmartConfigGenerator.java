package com.bdev.smart.config.generator;

import com.bdev.smart.config.data.inner.ConfigInfo;
import net.sourceforge.jenesis4java.VirtualMachine;
import net.sourceforge.jenesis4java.jaloppy.JenesisJalopyEncoder;

public class SmartConfigGenerator {
    public static void generate(
            String rootPath,
            ConfigInfo configInfo
    ) throws Exception {
        System.setProperty("jenesis.encoder", JenesisJalopyEncoder.class.getName());

        VirtualMachine vm = VirtualMachine.getVirtualMachine();

        SmartConfigDimensionsGenerator.generate(vm, rootPath, configInfo);
        SmartConfigPropertiesInterfaceGenerator.generate(vm, rootPath, configInfo);
        SmartConfigPropertiesGenerator.generate(vm, rootPath, configInfo);
    }
}
