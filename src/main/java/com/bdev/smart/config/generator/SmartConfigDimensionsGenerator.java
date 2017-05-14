package com.bdev.smart.config.generator;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.dimension.DimensionInfo;
import net.sourceforge.jenesis4java.*;

class SmartConfigDimensionsGenerator {
    static void generate(
            VirtualMachine vm,
            String rootPath,
            ConfigInfo configInfo
    ) throws Exception {
        CompilationUnit unit = vm.newCompilationUnit(rootPath);

        unit.setNamespace("com.bdev.smart.config");

        PackageClass smartConfigDimensionClass = unit.newPublicClass("SmartConfigDimension");

        for (String dimensionName : configInfo.getDimensions().keySet()) {
            Interface dimensionInterface = smartConfigDimensionClass
                    .newInnerInterface(dimensionName.toUpperCase());

            dimensionInterface.setAccess(Access.AccessType.PUBLIC);

            DimensionInfo dimensionInfo = configInfo.getDimensions().get(dimensionName);

            for (String dimensionValue : dimensionInfo.getDimensions()) {
                Constant dimensionConstant = dimensionInterface
                        .newConstant(vm.newType("String"), dimensionValue.toUpperCase());

                dimensionConstant.setExpression(vm.newString(dimensionValue.toUpperCase()));
            }
        }

        unit.encode();
    }
}
