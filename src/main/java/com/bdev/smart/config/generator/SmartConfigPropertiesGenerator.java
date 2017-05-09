package com.bdev.smart.config.generator;

import com.bdev.smart.config.data.inner.*;
import com.bdev.smart.config.data.util.Tuple;
import net.sourceforge.jenesis4java.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

class SmartConfigPropertiesGenerator {
    static void generate(
            VirtualMachine vm,
            String rootPath,
            ConfigInfo configInfo
    ) throws Exception {
        CompilationUnit unit = vm.newCompilationUnit(rootPath);

        unit.setNamespace("com.bdev.smart.config");

        unit.addImport(SmartConfigImports.SMART_CONFIG_IMPORT);

        PackageClass smartConfigProperties = unit.newClass("SmartConfigProperties");

        smartConfigProperties.setAccess(Access.PUBLIC);

        List<String> dimensionNames = new ArrayList<>(configInfo.getDimensions().keySet());

        f(vm, smartConfigProperties, configInfo, dimensionNames, 0, new Stack<>());

        generateGetConfigMethod(vm, smartConfigProperties, configInfo);

        unit.encode();
    }

    private static void f(
            VirtualMachine vm,
            PackageClass smartConfigProperties,
            ConfigInfo configInfo,
            List<String> dimensionNames,
            int dimensionIndex,
            Stack<String> dimensionValues
    )  {
        if (dimensionIndex == dimensionNames.size()) {
            InnerClass dimensionPropertyClass = smartConfigProperties.newInnerClass(getDimensionPropertiesName(dimensionValues));

            dimensionPropertyClass.addImplements("SmartConfig");

            dimensionPropertyClass.isStatic(true);

            for (String propertyName : configInfo.getPropertiesInfo().keySet()) {
                PropertyInfo propertyInfo = configInfo.getPropertiesInfo().get(propertyName);

                ClassMethod getPropertyMethod = dimensionPropertyClass.newMethod(
                        vm.newType(getType(propertyInfo.getType())),
                        getMethodName(propertyName)
                );

                getPropertyMethod.setAccess(Access.PUBLIC);

                DimensionPropertyInfo o = propertyInfo
                        .getDimensionPropertyInfo()
                        .iterator()
                        .next();

                if (propertyInfo.getType() == PropertyType.NUMBER) {
                    getPropertyMethod.newReturn().setExpression(vm.newVar("" + o.getValue()));
                }
            }
        } else {
            String dimensionName = dimensionNames.get(dimensionIndex);

            DimensionInfo dimensionInfo = configInfo.getDimensions().get(dimensionName);

            for (String dimensionValue : dimensionInfo.getDimensions()) {
                dimensionValues.push(dimensionValue);

                f(vm, smartConfigProperties, configInfo, dimensionNames, dimensionIndex + 1, dimensionValues);

                dimensionValues.pop();
            }
        }
    }

    private static String getDimensionPropertiesName(List<String> dimensionValues) {
        StringBuilder sb = new StringBuilder();

        for (String dimensionValue : dimensionValues) {
            sb.append(getDimensionValueName(dimensionValue));
        }

        sb.append("SmartConfig");

        return sb.toString();
    }

    private static String getDimensionValueName(String dimensionValue) {
        return
                dimensionValue.substring(0, 1).toUpperCase() +
                dimensionValue.substring(1);
    }

    private static void generateGetConfigMethod(
            VirtualMachine vm,
            PackageClass smartConfigProperties,
            ConfigInfo configInfo
    ) {
        ClassMethod getConfigMethod = smartConfigProperties.newMethod(
                vm.newType("com.bdev.smart.config.SmartConfig"),
                "getConfig"
        );

        getConfigMethod.setAccess(Access.PUBLIC);
        getConfigMethod.isStatic(true);

        for (String dimensionName : configInfo.getDimensions().keySet()) {
            getConfigMethod.addParameter(vm.newType("String"), dimensionName);
        }

        g(vm, getConfigMethod,
                configInfo,
                new ArrayList<>(configInfo.getDimensions().keySet()),
                0,
                new Stack<>()
        );

        getConfigMethod.newThrow(vm.newVar("new RuntimeException()"));
    }

    private static void g(
            VirtualMachine vm,
            ClassMethod getConfigMethod,
            ConfigInfo configInfo,
            List<String> dimensionNames,
            int dimensionIndex,
            Stack<Tuple<String, String>> dimensionValues
    )  {
        if (dimensionIndex == dimensionNames.size()) {
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
                    "new " +
                    getDimensionPropertiesName(dimensionValues
                            .stream()
                            .map(Tuple::getB)
                            .collect(Collectors.toList())
                    ) +
                    "()"
            ));
        } else {
            String dimensionName = dimensionNames.get(dimensionIndex);

            DimensionInfo dimensionInfo = configInfo.getDimensions().get(dimensionName);

            for (String dimensionValue : dimensionInfo.getDimensions()) {
                dimensionValues.push(new Tuple<>(dimensionName, dimensionValue));

                g(vm, getConfigMethod, configInfo, dimensionNames, dimensionIndex + 1, dimensionValues);

                dimensionValues.pop();
            }
        }
    }

    private static String getMethodName(String propertyName) {
        return "get" +
                propertyName.substring(0, 1).toUpperCase() +
                propertyName.substring(1);
    }

    private static String getType(PropertyType propertyType) {
        switch (propertyType) {
            case NUMBER:
                return "long";
            case BOOLEAN:
                return "boolean";
            case STRING:
                return "String";
            case LIST_OF_NUMBERS:
                return "java.util.List<Long>";
            case LIST_OF_BOOLEANS:
                return "java.util.List<Boolean>";
            case LIST_OF_STRINGS:
                return "java.util.List<String>";
        }

        throw new RuntimeException();
    }
}
