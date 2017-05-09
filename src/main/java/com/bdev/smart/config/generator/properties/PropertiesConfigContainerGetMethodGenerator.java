package com.bdev.smart.config.generator.properties;

import com.bdev.smart.config.data.inner.ConfigInfo;
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
        ClassMethod getConfigMethod =
                smartConfigProperties.newMethod(
                vm.newType("SmartConfig"),
                "getConfig"
        ); {
            getConfigMethod.setAccess(Access.PUBLIC);
            getConfigMethod.isStatic(true);
        }

        for (String dimensionName : configInfo.getDimensions().keySet()) {
            getConfigMethod.addParameter(vm.newType("String"), dimensionName);
        }

        PropertiesConfigGeneratorUtils.gatherDimensionsMultiplication(
                configInfo,
                new ArrayList<>(configInfo.getDimensions().keySet()),
                0,
                new Stack<>(),
                dimensionValues -> {
                    Expression e = null;

                    for (Tuple<String, String> dimensionValue : dimensionValues) {
                        Invoke invoke = vm.newInvoke(dimensionValue.getA(), "equals");

                        invoke.addArg(dimensionValue.getB());

                        if (e == null) {
                            e = invoke;
                        } else {
                            e = vm.newBinary(Binary.AND,
                                    e,
                                    invoke
                            );
                        }
                    }

                    If chooseConfigIf = getConfigMethod.newIf(e);

                    chooseConfigIf.newReturn().setExpression(vm.newVar(
                            SmartConfigNamesMatcher.getDimensionPropertiesInstanceName(dimensionValues)
                    ));
                }
        );

        getConfigMethod.newThrow(vm.newVar("new RuntimeException()"));
    }
}
