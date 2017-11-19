package com.bdev.smart.config.generator.properties;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.dimension.Dimension;
import com.bdev.smart.config.data.inner.dimension.DimensionValue;
import com.bdev.smart.config.data.inner.dimension.Point;
import com.bdev.smart.config.data.util.Tuple;
import com.bdev.smart.config.generator.utils.SmartConfigNamesMatcher;
import net.sourceforge.jenesis4java.*;

import java.util.ArrayList;
import java.util.Stack;

class PropertiesConfigContainerGetMethodGenerator {
    static void generate(
            VirtualMachine vm,
            PackageClass smartConfigProperties,
            ConfigInfo configInfo
    ) {
        ClassMethod getConfigMethod = smartConfigProperties.newMethod(
                vm.newType("SmartConfig"),
                "getConfig"
        ); {
            getConfigMethod.setAccess(Access.PUBLIC);
            getConfigMethod.isStatic(true);
        }

        for (Dimension dimension: configInfo.getSpaceInfo().getSpace().getDimensions()) {
            getConfigMethod.addParameter(vm.newType("String"), dimension.getName());
        }

        for (Point point: configInfo.getSpaceInfo().getPoints()) {
            Expression e = null;

            for (Dimension dimension: point.getLocation().keySet()) {
                DimensionValue dimensionValue = point.getLocation().get(dimension);

                Invoke invoke = vm.newInvoke(dimension.getName(), "equals");

                invoke.addArg(dimensionValue.getName());

                if (e == null) {
                    e = invoke;
                } else {
                    e = vm.newBinary(Binary.AND, e, invoke);
                }
            }

            If chooseConfigIf = getConfigMethod.newIf(e);

            chooseConfigIf.newReturn().setExpression(vm.newVar(
                    point.getName() + "SmartConfig"
            ));
        }

        getConfigMethod.newThrow(vm.newVar("new RuntimeException()"));
    }
}
