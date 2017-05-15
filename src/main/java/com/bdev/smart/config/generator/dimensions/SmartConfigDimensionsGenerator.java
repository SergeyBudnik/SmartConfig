package com.bdev.smart.config.generator.dimensions;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.dimension.Dimension;
import net.sourceforge.jenesis4java.*;

public class SmartConfigDimensionsGenerator {
    public static void generate(
            VirtualMachine vm,
            String rootPath,
            ConfigInfo configInfo
    ) throws Exception {
        CompilationUnit unit = vm.newCompilationUnit(rootPath);

        unit.setNamespace("com.bdev.smart.config");

        PackageClass smartConfigDimensionClass = unit.newPublicClass("SmartConfigDimension");

        for (String dimensionName : configInfo.getAllDimensions().getDimensions().keySet()) {
            Interface dimensionInterface = smartConfigDimensionClass
                    .newInnerInterface(dimensionName.toUpperCase());

            dimensionInterface.setAccess(Access.AccessType.PUBLIC);

            Dimension dimension = configInfo.getAllDimensions().getDimensions().get(dimensionName);

            for (String dimensionValue : dimension.getValues()) {
                Constant dimensionConstant = dimensionInterface
                        .newConstant(vm.newType("String"), dimensionValue.toUpperCase());

                dimensionConstant.setExpression(vm.newString(dimensionValue.toUpperCase()));
            }
        }

        unit.encode();
    }
}
