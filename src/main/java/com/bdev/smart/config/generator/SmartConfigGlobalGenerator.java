package com.bdev.smart.config.generator;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.dimension.Point;
import com.bdev.smart.config.generator.dimensions.SmartConfigSpaceGenerator;
import com.bdev.smart.config.generator.properties.SmartConfigGenerator;
import com.bdev.smart.config.generator.properties.SmartConfigPropertiesGenerator;
import com.bdev.smart.config.generator.properties.SmartConfigPointGenerator;
import net.sourceforge.jenesis4java.VirtualMachine;
import net.sourceforge.jenesis4java.jaloppy.JenesisJalopyEncoder;

public class SmartConfigGlobalGenerator {
    public static void generate(
            String rootPath,
            String rootPackage,
            ConfigInfo configInfo
    ) {
        System.setProperty("jenesis.encoder", JenesisJalopyEncoder.class.getName());

        VirtualMachine vm = VirtualMachine.getVirtualMachine();

        SmartConfigSpaceGenerator.generate(vm, rootPath, rootPackage, configInfo.getSpaceInfo().getSpace());
        SmartConfigGenerator.generate(vm, rootPath, rootPackage, configInfo);
        SmartConfigPropertiesGenerator.generate(vm, rootPath, rootPackage, configInfo);

        for (Point point : configInfo.getSpaceInfo().getPoints()) {
            SmartConfigPointGenerator.generate(vm, rootPath, rootPackage, configInfo, point);
        }
    }
}
