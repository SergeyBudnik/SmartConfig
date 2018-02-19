package com.bdev.smart.config.generator.dimensions;

import com.bdev.smart.config.data.inner.dimension.Dimension;
import com.bdev.smart.config.data.inner.dimension.DimensionValue;
import com.bdev.smart.config.data.inner.dimension.Space;
import com.bdev.smart.config.generator.SmartConfigGlobalGenerator;
import net.sourceforge.jenesis4java.*;

public class SmartConfigSpaceGenerator {
    public static void generate(VirtualMachine vm, String rootPath, String rootPackage, Space space) {
        CompilationUnit unit = vm.newCompilationUnit(rootPath);

        unit.setNamespace(rootPackage);

        PackageClass smartConfigDimensionClass = unit.newPublicClass("SmartConfigSpace");

        for (Dimension dimension: space.getDimensions()) {
            Interface dimensionInterface = smartConfigDimensionClass.newInnerInterface(
                    getInterfaceName(dimension)
            );

            dimensionInterface.setAccess(Access.AccessType.PUBLIC);

            for (DimensionValue dimensionValue : dimension.getValues()) {
                Constant dimensionConstant = dimensionInterface
                        .newConstant(vm.newType("String"), dimensionValue.getName().toUpperCase());

                dimensionConstant.setExpression(vm.newString(
                        dimensionValue.getName()
                ));
            }
        }

        unit.encode();
    }

    private static String getInterfaceName(Dimension dimension) {
        return
                dimension.getName().substring(0, 1).toUpperCase() +
                dimension.getName().substring(1);
    }
}
