package com.bdev.smart.config.generator;

import com.bdev.smart.config.data.inner.*;
import com.bdev.smart.config.data.util.Tuple;
import com.bdev.smart.config.generator.utils.SmartConfigImports;
import com.bdev.smart.config.generator.utils.SmartConfigNamesMatcher;
import com.bdev.smart.config.generator.utils.SmartConfigNamespace;
import com.bdev.smart.config.generator.utils.SmartConfigTypesMatcher;
import net.sourceforge.jenesis4java.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

class SmartConfigPropertiesGenerator {
    static void generate(
            VirtualMachine vm,
            String rootPath,
            ConfigInfo configInfo
    ) throws Exception {
        CompilationUnit unit = vm.newCompilationUnit(rootPath);

        unit.setNamespace(SmartConfigNamespace.VALUE);

        unit.addImport(SmartConfigImports.LIST_IMPORT);
        unit.addImport(SmartConfigImports.ARRAYS_IMPORT);

        unit.addImport(SmartConfigImports.SMART_CONFIG_IMPORT);
        unit.addImport(SmartConfigImports.SMART_CONFIG_VALUE_IMPORT);

        PackageClass smartConfigProperties = unit.newClass("SmartConfigProperties");

        smartConfigProperties.setAccess(Access.PUBLIC);

        generateDimensionProperties(vm, smartConfigProperties, configInfo);
        generateGetConfigMethod(vm, smartConfigProperties, configInfo);

        unit.encode();
    }

    private static void gatherDimensionsMultiplication(
            ConfigInfo configInfo,
            List<String> dimensionNames,
            int dimensionIndex,
            Stack<Tuple<String, String>> dimensionValues,
            Consumer<Stack<Tuple<String, String>>> action
    ) {
        if (dimensionIndex == dimensionNames.size()) {
            action.accept(dimensionValues);
        } else {
            String dimensionName = dimensionNames.get(dimensionIndex);

            DimensionInfo dimensionInfo = configInfo.getDimensions().get(dimensionName);

            for (String dimensionValue : dimensionInfo.getDimensions()) {
                dimensionValues.push(new Tuple<>(dimensionName, dimensionValue));

                gatherDimensionsMultiplication(
                        configInfo,
                        dimensionNames,
                        dimensionIndex + 1,
                        dimensionValues,
                        action
                );

                dimensionValues.pop();
            }
        }
    }

    private static void generateDimensionProperties(
            VirtualMachine vm,
            PackageClass smartConfigPropertiesClass,
            ConfigInfo configInfo
    ) {
        List<String> dimensionNames = new ArrayList<>(configInfo.getDimensions().keySet());

        Consumer<Stack<Tuple<String, String>>> generator = dimensionValues -> {
            String dimensionPropertiesName = getDimensionPropertiesName(dimensionValues);

            InnerClass dimensionPropertyClass = smartConfigPropertiesClass
                    .newInnerClass(dimensionPropertiesName);

            dimensionPropertyClass.addImplements("SmartConfig");

            dimensionPropertyClass.isStatic(true);

            for (String propertyName : configInfo.getPropertiesInfo().keySet()) {
                PropertyInfo propertyInfo = configInfo.getPropertiesInfo().get(propertyName);

                DimensionPropertyInfo dimensionPropertyInfo = propertyInfo
                        .getMostSuitableProperty(dimensionValues);

                ClassField f = dimensionPropertyClass.newField(
                        vm.newType(SmartConfigTypesMatcher.getType(propertyInfo.getType())),
                        propertyName
                );

                f.setExpression(getPropertyValue(vm, dimensionPropertyInfo));

                ClassMethod getPropertyMethod = dimensionPropertyClass.newMethod(
                        vm.newType(SmartConfigTypesMatcher.getType(propertyInfo.getType())),
                        SmartConfigNamesMatcher.getPropertyAccessorName(propertyName)
                );

                getPropertyMethod.setAccess(Access.PUBLIC);

                getPropertyMethod.newReturn().setExpression(vm.newVar(propertyName));
            }

            smartConfigPropertiesClass.newField(vm.newType(dimensionPropertiesName), dimensionPropertiesName);
        };

        gatherDimensionsMultiplication(
                configInfo,
                dimensionNames,
                0,
                new Stack<>(),
                generator
        );
    }

    private static String getDimensionPropertiesName(
            Stack<Tuple<String, String>> dimensionValues
    ) {
        StringBuilder sb = new StringBuilder();

        for (Tuple<String, String> dimensionValue : dimensionValues) {
            sb.append(getDimensionValueName(dimensionValue.getB()));
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
                    getDimensionPropertiesName(dimensionValues) +
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

    private static Expression getPropertyValue(
            VirtualMachine vm,
            DimensionPropertyInfo dimensionPropertyInfo
    ) {
        return vm.newVar(
                "new SmartConfigValue(" +
                getUnboxedPropertyValue(dimensionPropertyInfo) +
                ")"
        );
    }

    private static String getUnboxedPropertyValue(
            DimensionPropertyInfo dimensionPropertyInfo
    ) {
        switch (dimensionPropertyInfo.getType()) {
            case NUMBER:
            case BOOLEAN:
                return "" + dimensionPropertyInfo.getValue();
            case STRING:
                return "\"" + dimensionPropertyInfo.getValue() + "\"";
            case LIST_OF_STRINGS: {
                StringBuilder sb = new StringBuilder();

                sb.append("Arrays.asList(");

                for (Object o : (List) dimensionPropertyInfo.getValue()) {
                    sb.append("\"");
                    sb.append(o);
                    sb.append("\"");
                    sb.append(", ");
                }

                sb.deleteCharAt(sb.length() - 1);
                sb.deleteCharAt(sb.length() - 1);

                sb.append(")");

                System.out.println(sb.toString());

                return sb.toString();
            }
            case LIST_OF_NUMBERS:
            case LIST_OF_BOOLEANS: {
                StringBuilder sb = new StringBuilder();

                sb.append("Arrays.asList(");

                for (Object o : (List) dimensionPropertyInfo.getValue()) {
                    sb.append(o);
                    sb.append(", ");
                }

                sb.deleteCharAt(sb.length() - 1);
                sb.deleteCharAt(sb.length() - 1);

                sb.append(")");

                System.out.println(sb.toString());

                return sb.toString();
            }
        }

        throw new RuntimeException();
    }
}
