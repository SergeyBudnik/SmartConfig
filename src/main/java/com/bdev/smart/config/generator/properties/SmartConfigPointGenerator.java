package com.bdev.smart.config.generator.properties;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.dimension.Point;
import com.bdev.smart.config.data.inner.property.ConditionalProperty;
import com.bdev.smart.config.data.inner.property.Property;
import com.bdev.smart.config.data.inner.property.PropertyType;
import com.bdev.smart.config.generator.utils.SmartConfigImports;
import com.bdev.smart.config.generator.utils.SmartConfigNames;
import net.sourceforge.jenesis4java.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class SmartConfigPointGenerator {
    public static void generate(VirtualMachine vm, String rootPath, String rootPackage, ConfigInfo configInfo, Point point) {
        CompilationUnit unit = vm.newCompilationUnit(rootPath);

        unit.setNamespace(rootPackage);

        unit.addImport(SmartConfigImports.LIST_IMPORT);
        unit.addImport(SmartConfigImports.COLLECTIONS_IMPORT);
        unit.addImport(SmartConfigImports.ARRAYS_IMPORT);
        unit.addImport(SmartConfigImports.OPTIONAL_IMPORT);

        unit.addImport(rootPackage + "." + SmartConfigImports.SMART_CONFIG_IMPORT);
        unit.addImport(SmartConfigImports.SMART_CONFIG_VALUE_IMPORT);

        String pointPropertyClassName = point.getName() + "SmartConfig";

        PackageClass pointPropertyClass = unit.newClass(pointPropertyClassName);

        pointPropertyClass.setAccess(Access.PACKAGE);
        pointPropertyClass.setExtends("SmartConfigImpl");

        generateConstructor(vm, pointPropertyClass, configInfo, point);

        unit.encode();
    }

    private static void generateConstructor(
            VirtualMachine vm, PackageClass pointPropertyClass, ConfigInfo configInfo, Point point
    ) {
        Constructor constructor = pointPropertyClass.newConstructor();

        List<String> propertiesNames = configInfo
                .getAllProperties()
                .getAllProperties()
                .keySet()
                .stream()
                .sorted()
                .collect(toList());

        for (String propertyName : propertiesNames) {
            Property property = configInfo.getAllProperties().getAllProperties().get(propertyName);

            ConditionalProperty conditionalProperty = property.getMostSuitableProperty(point);

            constructor.newStmt(
                    vm.newInvoke(
                            SmartConfigNames.getPropertyConfigSetterName(propertyName)
                    ).addArg(getPropertyValue(vm, conditionalProperty))
            );
        }
    }

    private static Expression getPropertyValue(VirtualMachine vm, ConditionalProperty conditionalProperty) {
        return vm.newVar(
                "new SmartConfigValue(" +
                        "\"" + conditionalProperty.getName() + "\"" +
                        ", " +
                        getUnboxedPropertyValue(conditionalProperty) +
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

        throw new RuntimeException("Unexpected property type: '" + conditionalProperty.getType() + "'");
    }
}
