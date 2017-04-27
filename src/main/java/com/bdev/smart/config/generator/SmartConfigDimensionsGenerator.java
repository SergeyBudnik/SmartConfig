package com.bdev.smart.config.generator;

import com.bdev.smart.config.factory.SmartConfigDimensionsFactory;
import net.sourceforge.jenesis4java.*;

import java.util.Map;
import java.util.Set;

public class SmartConfigDimensionsGenerator {
    public static void generate(VirtualMachine vm, String rootPath, String dimensionsPath) throws Exception {
        Map<String, Set<String>> dimensions = SmartConfigDimensionsFactory.produce(dimensionsPath);

        generate(vm, rootPath, dimensions);
    }

    private static void generate(
            VirtualMachine vm,
            String rootPath,
            Map<String, Set<String>> dimensions
    ) throws Exception {
        CompilationUnit unit = vm.newCompilationUnit(rootPath);

        unit.setNamespace("com.bdev.smart.config");

        PackageClass smartConfigDimensionClass = unit.newPublicClass("SmartConfigDimension");

        for (String dimensionName : dimensions.keySet()) {
            Interface dimensionInterface = smartConfigDimensionClass
                    .newInnerInterface(dimensionName.toUpperCase());

            dimensionInterface.setAccess(Access.AccessType.PUBLIC);

            for (String dimensionValue : dimensions.get(dimensionName)) {
                Constant dimensionConstant = dimensionInterface
                        .newConstant(vm.newType("String"), dimensionValue.toUpperCase());

                dimensionConstant.setExpression(vm.newString(dimensionValue.toUpperCase()));
            }
        }

        unit.encode();
    }
}
