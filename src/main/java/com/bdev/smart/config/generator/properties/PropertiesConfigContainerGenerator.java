package com.bdev.smart.config.generator.properties;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.dimension.Point;
import com.bdev.smart.config.data.inner.property.ConditionalProperty;
import com.bdev.smart.config.data.inner.property.Property;
import com.bdev.smart.config.data.inner.property.PropertyType;
import com.bdev.smart.config.generator.utils.SmartConfigImports;
import com.bdev.smart.config.generator.utils.SmartConfigNamesMatcher;
import com.bdev.smart.config.generator.utils.SmartConfigNamespace;
import com.bdev.smart.config.generator.utils.SmartConfigTypesMatcher;
import net.sourceforge.jenesis4java.*;

import java.util.List;
import java.util.Set;

public class PropertiesConfigContainerGenerator {
    public static void generate(
            VirtualMachine vm,
            String rootPath,
            ConfigInfo configInfo
    ) throws Exception {
        CompilationUnit unit = vm.newCompilationUnit(rootPath);

        unit.setNamespace(SmartConfigNamespace.VALUE);

        unit.addImport(SmartConfigImports.LIST_IMPORT);
        unit.addImport(SmartConfigImports.COLLECTIONS_IMPORT);
        unit.addImport(SmartConfigImports.ARRAYS_IMPORT);
        unit.addImport(SmartConfigImports.OPTIONAL_IMPORT);

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
        Set<Point> points = configInfo.getSpaceInfo().getPoints();

        for (Point point : points) {
            String dimensionPropertiesName = point.getName() + "SmartConfig";

            InnerClass dimensionPropertyClass = smartConfigPropertiesClass
                    .newInnerClass(dimensionPropertiesName);

            dimensionPropertyClass.addImplements("SmartConfig");

            dimensionPropertyClass.setAccess(Access.PRIVATE);
            dimensionPropertyClass.isStatic(true);

            for (String propertyName : configInfo.getAllProperties().getAllProperties().keySet()) {
                Property property = configInfo.getAllProperties().getAllProperties().get(propertyName);

                ConditionalProperty conditionalProperty = property.getMostSuitableProperty(point);

                ClassField f = dimensionPropertyClass.newField(
                        vm.newType(SmartConfigTypesMatcher.getType(property.getType())),
                        propertyName
                );

                f.setAccess(Access.PRIVATE);
                f.setExpression(getPropertyValue(vm, conditionalProperty));

                ClassMethod getPropertyMethod = dimensionPropertyClass.newMethod(
                        vm.newType(SmartConfigTypesMatcher.getType(property.getType())),
                        SmartConfigNamesMatcher.getPropertyAccessorName(propertyName)
                );

                getPropertyMethod.setAccess(Access.PUBLIC);

                getPropertyMethod.newReturn().setExpression(vm.newVar(propertyName));
            }

            PropertiesConfigContainerPropertyAllPropertiesFieldGenerator
                    .generate(vm, dimensionPropertyClass, configInfo.getAllProperties());

            PropertiesConfigContainerPropertyFindPropertyByNameMethodGenerator
                    .generate(vm, dimensionPropertyClass);

            Field smartConfigPropertiesInstance = smartConfigPropertiesClass.newField(
                    vm.newType(dimensionPropertiesName),
                    dimensionPropertiesName // ToDo
            );

            smartConfigPropertiesInstance.setAccess(Access.PRIVATE);
            smartConfigPropertiesInstance.isStatic(true);

            smartConfigPropertiesInstance.setExpression(vm.newVar(
                    "new " +
                            dimensionPropertiesName +
                    "()"
            ));
        }
    }

    private static Expression getPropertyValue(
            VirtualMachine vm,
            ConditionalProperty conditionalProperty
    ) {
        return vm.newVar(
                "new SmartConfigValue(" +
                    "\"" + conditionalProperty.getName() + "\"" +
                    ", " +
                    getUnboxedPropertyValue(conditionalProperty) +
                    ", " +
                    Boolean.toString(conditionalProperty.getParent().isReadProtected()) +
                    ", " +
                    Boolean.toString(conditionalProperty.getParent().isOverrideProtected()) +
                ")"
        );
    }

    private static String getUnboxedPropertyValue(
            ConditionalProperty conditionalProperty
    ) {
        switch (conditionalProperty.getType()) {
            case NUMBER:
                return "" + conditionalProperty.getValue() + "L";
            case BOOLEAN:
                return "" + conditionalProperty.getValue();
            case STRING:
                return "\"" + conditionalProperty.getValue() + "\"";
            case LIST_OF_STRINGS:
            case LIST_OF_NUMBERS:
            case LIST_OF_BOOLEANS: {
                StringBuilder sb = new StringBuilder();

                sb.append("Collections.unmodifiableList(Arrays.asList(");

                for (Object o : (List) conditionalProperty.getValue()) {
                    if (conditionalProperty.getType() == PropertyType.LIST_OF_STRINGS) {
                        sb.append("\"");
                        sb.append(o);
                        sb.append("\"");
                    } else if (conditionalProperty.getType() == PropertyType.LIST_OF_NUMBERS) {
                        sb.append(o);
                        sb.append("L");
                    } else {
                        sb.append(o);
                    }

                    sb.append(", ");
                }

                sb.deleteCharAt(sb.length() - 1);
                sb.deleteCharAt(sb.length() - 1);

                sb.append("))");

                return sb.toString();
            }
        }

        throw new RuntimeException();
    }
}
