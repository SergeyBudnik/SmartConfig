package com.bdev.smart.config.generator;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.generator.dimensions.SmartConfigDimensionsGenerator;
import com.bdev.smart.config.generator.properties.PropertiesConfigContainerGenerator;
import com.bdev.smart.config.generator.properties.PropertiesConfigGenerator;
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
        PropertiesConfigGenerator.generate(vm, rootPath, configInfo);
        PropertiesConfigContainerGenerator.generate(vm, rootPath, configInfo);
    }
}
