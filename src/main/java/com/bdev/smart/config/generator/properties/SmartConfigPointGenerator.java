package com.bdev.smart.config.generator.properties;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.dimension.Point;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.data.inner.property.ConditionalProperty;
import com.bdev.smart.config.data.inner.property.Property;
import com.bdev.smart.config.data.inner.property.PropertyType;
import com.bdev.smart.config.generator.utils.SmartConfigImports;
import com.bdev.smart.config.generator.utils.SmartConfigNames;
import com.bdev.smart.config.generator.utils.SmartConfigNamespace;
import com.bdev.smart.config.generator.utils.SmartConfigTypesMatcher;
import net.sourceforge.jenesis4java.*;

import java.util.List;

public class SmartConfigPointGenerator {
    public static void generate(VirtualMachine vm, String rootPath, ConfigInfo configInfo, Point point) {
        CompilationUnit unit = vm.newCompilationUnit(rootPath);

        unit.setNamespace(SmartConfigNamespace.VALUE);

        unit.addImport(SmartConfigImports.LIST_IMPORT);
        unit.addImport(SmartConfigImports.COLLECTIONS_IMPORT);
        unit.addImport(SmartConfigImports.ARRAYS_IMPORT);
        unit.addImport(SmartConfigImports.OPTIONAL_IMPORT);

        unit.addImport(SmartConfigImports.SMART_CONFIG_IMPORT);
        unit.addImport(SmartConfigImports.SMART_CONFIG_VALUE_IMPORT);

        String pointPropertyClassName = point.getName() + "SmartConfig";

        PackageClass pointPropertyClass = unit.newClass(pointPropertyClassName);

        pointPropertyClass.setAccess(Access.PACKAGE);
        pointPropertyClass.addImplements("SmartConfig");

        for (String propertyName : configInfo.getAllProperties().getAllProperties().keySet()) {
            Property property = configInfo.getAllProperties().getAllProperties().get(propertyName);

            ConditionalProperty conditionalProperty = property.getMostSuitableProperty(point);

            ClassField f = pointPropertyClass.newField(
                    vm.newType(SmartConfigTypesMatcher.getType(property.getType())),
                    propertyName
            );

            f.setAccess(Access.PRIVATE);
            f.setExpression(getPropertyValue(vm, conditionalProperty));

            ClassMethod getPropertyMethod = pointPropertyClass.newMethod(
                    vm.newType(SmartConfigTypesMatcher.getType(property.getType())),
                    SmartConfigNames.getPropertyAccessorName(propertyName)
            );

            getPropertyMethod.setAccess(Access.PUBLIC);

            getPropertyMethod.newReturn().setExpression(vm.newVar(propertyName));
        }

        generateAllPropertiesArray(vm, pointPropertyClass, configInfo.getAllProperties());
        generateFindPropertyByNameMethod(vm, pointPropertyClass);

        unit.encode();
    }

    private static void generateFindPropertyByNameMethod(
            VirtualMachine vm,
            PackageClass dimensionPropertyClass
    ) {
        ClassMethod findPropertyByNameMethod = dimensionPropertyClass.newMethod(
                vm.newType("<T> Optional<SmartConfigValue<T>>"),
                "findPropertyByName"
        );

        findPropertyByNameMethod.setAccess(Access.PUBLIC);
        findPropertyByNameMethod.isFinal(true);

        findPropertyByNameMethod.addParameter(vm.newType("String"), "propertyName");

        For allPropertiesIteration = findPropertyByNameMethod.newFor();

        allPropertiesIteration.addInit(vm.newVar("int i = 0"));
        allPropertiesIteration.setPredicate(vm.newVar("i < ALL_PROPERTIES.size()"));
        allPropertiesIteration.addUpdate(vm.newVar("i++"));

        If allPropertiesIterationIf = allPropertiesIteration
                .newIf(vm.newVar("ALL_PROPERTIES.get(i).isNameSuitable(propertyName)"));

        allPropertiesIterationIf
                .newReturn()
                .setExpression(vm.newVar("Optional.of(ALL_PROPERTIES.get(i))"));

        findPropertyByNameMethod.newReturn().setExpression(vm.newVar("Optional.empty()"));
    }

    private static Expression getPropertyValue(VirtualMachine vm, ConditionalProperty conditionalProperty) {
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

    private static void generateAllPropertiesArray(
            VirtualMachine vm,
            PackageClass dimensionPropertyClass,
            AllProperties allProperties
    ) {
        ClassField allPropertiesField = dimensionPropertyClass.newField(
                vm.newType("List<SmartConfigValue>"),
                "ALL_PROPERTIES"
        );

        allPropertiesField.setAccess(Access.PRIVATE);
        allPropertiesField.isFinal(true);

        StringBuilder expression = new StringBuilder("Arrays.asList(");

        for (String propertyName : allProperties.getAllProperties().keySet()) {
            expression.append(propertyName);
            expression.append(", ");
        }

        expression.deleteCharAt(expression.length() - 1);
        expression.deleteCharAt(expression.length() - 1);

        expression.append(")");

        allPropertiesField.setExpression(vm.newVar(expression.toString()));
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
