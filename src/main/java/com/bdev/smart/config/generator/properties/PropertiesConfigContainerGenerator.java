package com.bdev.smart.config.generator.properties;

import com.bdev.smart.config.data.inner.*;
import com.bdev.smart.config.data.inner.property.DimensionPropertyInfo;
import com.bdev.smart.config.data.inner.property.PropertyInfo;
import com.bdev.smart.config.data.inner.property.PropertyType;
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

public class PropertiesConfigContainerGenerator {
    public static void generate(
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

        PropertiesConfigContainerGetMethodGenerator
                .generate(vm, smartConfigProperties, configInfo);

        unit.encode();
    }

    private static void generateDimensionProperties(
            VirtualMachine vm,
            PackageClass smartConfigPropertiesClass,
            ConfigInfo configInfo
    ) {
        List<String> dimensionNames = new ArrayList<>(configInfo.getDimensions().keySet());

        Consumer<Stack<Tuple<String, String>>> generator = dimensionValues -> {
            String dimensionPropertiesName = SmartConfigNamesMatcher
                    .getDimensionPropertiesClassName(dimensionValues);

            InnerClass dimensionPropertyClass = smartConfigPropertiesClass
                    .newInnerClass(dimensionPropertiesName);

            dimensionPropertyClass.addImplements("SmartConfig");

            dimensionPropertyClass.setAccess(Access.PRIVATE);
            dimensionPropertyClass.isStatic(true);

            for (String propertyName : configInfo.getPropertiesInfo().keySet()) {
                PropertyInfo propertyInfo = configInfo.getPropertiesInfo().get(propertyName);

                DimensionPropertyInfo dimensionPropertyInfo = propertyInfo
                        .getMostSuitableProperty(dimensionValues);

                ClassField f = dimensionPropertyClass.newField(
                        vm.newType(SmartConfigTypesMatcher.getType(propertyInfo.getType())),
                        propertyName
                );

                f.setAccess(Access.PRIVATE);
                f.setExpression(getPropertyValue(vm, dimensionPropertyInfo));

                ClassMethod getPropertyMethod = dimensionPropertyClass.newMethod(
                        vm.newType(SmartConfigTypesMatcher.getType(propertyInfo.getType())),
                        SmartConfigNamesMatcher.getPropertyAccessorName(propertyName)
                );

                getPropertyMethod.setAccess(Access.PUBLIC);

                getPropertyMethod.newReturn().setExpression(vm.newVar(propertyName));
            }

            Field smartConfigPropertiesInstance = smartConfigPropertiesClass.newField(
                    vm.newType(dimensionPropertiesName),
                    SmartConfigNamesMatcher.getDimensionPropertiesInstanceName(dimensionValues)
            );

            smartConfigPropertiesInstance.setAccess(Access.PRIVATE);
            smartConfigPropertiesInstance.isStatic(true);

            smartConfigPropertiesInstance.setExpression(vm.newVar(
                    "new " +
                        SmartConfigNamesMatcher.getDimensionPropertiesClassName(dimensionValues) +
                    "()"
            ));
        };

        PropertiesConfigGeneratorUtils.gatherDimensionsMultiplication(
                configInfo,
                dimensionNames,
                0,
                new Stack<>(),
                generator
        );
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
            case LIST_OF_STRINGS:
            case LIST_OF_NUMBERS:
            case LIST_OF_BOOLEANS: {
                StringBuilder sb = new StringBuilder();

                sb.append("Arrays.asList(");

                for (Object o : (List) dimensionPropertyInfo.getValue()) {
                    if (dimensionPropertyInfo.getType() == PropertyType.LIST_OF_STRINGS) {
                        sb.append("\"");
                        sb.append(o);
                        sb.append("\"");
                    } else {
                        sb.append(o);
                    }

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
