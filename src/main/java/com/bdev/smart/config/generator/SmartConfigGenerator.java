package com.bdev.smart.config.generator;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.dimension.Point;
import com.bdev.smart.config.generator.dimensions.SmartConfigSpaceGenerator;
import com.bdev.smart.config.generator.properties.SmartConfigPropertiesGenerator;
import com.bdev.smart.config.generator.properties.SmartConfigPointGenerator;
import net.sourceforge.jenesis4java.VirtualMachine;
import net.sourceforge.jenesis4java.jaloppy.JenesisJalopyEncoder;

public class SmartConfigGenerator {
    public static final String BASE_PACKAGE = "com.bdev.smart.config";

    public static void generate(
            String rootPath,
            ConfigInfo configInfo
    ) throws Exception {
        System.setProperty("jenesis.encoder", JenesisJalopyEncoder.class.getName());

        VirtualMachine vm = VirtualMachine.getVirtualMachine();

        SmartConfigSpaceGenerator
                .generate(vm, rootPath, configInfo.getSpaceInfo().getSpace());

        com.bdev.smart.config.generator.properties.SmartConfigGenerator.generate(vm, rootPath, configInfo);
        SmartConfigPropertiesGenerator.generate(vm, rootPath, configInfo);

        for (Point point : configInfo.getSpaceInfo().getPoints()) {
            SmartConfigPointGenerator.generate(vm, rootPath, configInfo, point);
        }
    }
}
