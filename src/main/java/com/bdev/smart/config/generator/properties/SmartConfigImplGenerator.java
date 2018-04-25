package com.bdev.smart.config.generator.properties;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.property.AllProperties;
import com.bdev.smart.config.data.inner.property.Property;
import com.bdev.smart.config.generator.utils.SmartConfigImports;
import com.bdev.smart.config.generator.utils.SmartConfigNames;
import com.bdev.smart.config.generator.utils.SmartConfigTypesMatcher;
import net.sourceforge.jenesis4java.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class SmartConfigImplGenerator {
    public static void generate(VirtualMachine vm, String rootPath, String rootPackage, ConfigInfo configInfo) {
        CompilationUnit unit = vm.newCompilationUnit(rootPath);

        unit.setNamespace(rootPackage);

        unit.addImport(SmartConfigImports.LIST_IMPORT);
        unit.addImport(SmartConfigImports.COLLECTIONS_IMPORT);
        unit.addImport(SmartConfigImports.ARRAYS_IMPORT);
        unit.addImport(SmartConfigImports.OPTIONAL_IMPORT);

        unit.addImport(rootPackage + "." + SmartConfigImports.SMART_CONFIG_IMPORT);
        unit.addImport(SmartConfigImports.SMART_CONFIG_VALUE_IMPORT);

        PackageClass pointPropertyClass = unit.newClass("SmartConfigImpl");

        pointPropertyClass.setAccess(Access.PACKAGE);
        pointPropertyClass.addImplements("SmartConfig");

        for (String propertyName : configInfo.getAllProperties().getAllProperties().keySet()) {
            Property property = configInfo.getAllProperties().getAllProperties().get(propertyName);

            ClassField f = pointPropertyClass.newField(
                    vm.newType(SmartConfigTypesMatcher.getConfigType(property.getType())),
                    propertyName
            );

            f.setAccess(Access.PRIVATE);

            ClassMethod getPropertyConfigMethod = pointPropertyClass.newMethod(
                    vm.newType(SmartConfigTypesMatcher.getConfigType(property.getType())),
                    SmartConfigNames.getPropertyConfigAccessorName(propertyName)
            ); {
                getPropertyConfigMethod.setAccess(Access.PUBLIC);
                getPropertyConfigMethod.newReturn().setExpression(vm.newVar(propertyName));
            }

            ClassMethod getPropertyMethod = pointPropertyClass.newMethod(
                    vm.newType(SmartConfigTypesMatcher.getType(property.getType(), true)),
                    SmartConfigNames.getPropertyAccessorName(propertyName)
            ); {
                getPropertyMethod.setAccess(Access.PUBLIC);
                getPropertyMethod.newReturn().setExpression(
                        vm.newVar(propertyName + ".getValue()")
                );
            }
        }

        generateConstructor(vm, pointPropertyClass, configInfo.getAllProperties());
        generateCopyMethod(vm, pointPropertyClass, configInfo.getAllProperties());
        generateAllPropertiesArray(vm, pointPropertyClass, configInfo.getAllProperties());
        generateFindPropertyByNameMethod(vm, pointPropertyClass);

        unit.encode();
    }

    private static void generateConstructor(
            VirtualMachine vm,
            PackageClass dimensionPropertyClass,
            AllProperties allProperties
    ) {
        Constructor constructor = dimensionPropertyClass.newConstructor();

        constructor.setAccess(Access.PUBLIC);

        List<String> propertiesNames = allProperties
                        .getAllProperties()
                        .keySet()
                        .stream()
                        .sorted()
                        .collect(toList());

        for (String propertyName : propertiesNames) {
            Property property = allProperties.getAllProperties().get(propertyName);

            constructor.addParameter(
                    vm.newType(SmartConfigTypesMatcher.getConfigType(property.getType())),
                    propertyName
            );

            constructor.newStmt(vm.newAssign(
                    vm.newVar("this." + propertyName),
                    vm.newVar(propertyName)
            ));
        }
    }

    private static void generateCopyMethod(
            VirtualMachine vm,
            PackageClass dimensionPropertyClass,
            AllProperties allProperties
    ) {
        ClassMethod copyMethod = dimensionPropertyClass.newMethod(
                vm.newType("SmartConfig"),
                "copy"
        );

        copyMethod.setAccess(Access.PUBLIC);

        List<String> propertiesNames = allProperties
                .getAllProperties()
                .keySet()
                .stream()
                .sorted()
                .collect(toList());

        StringBuilder sb = new StringBuilder();

        for (String propertyName : propertiesNames) {
            if (sb.length() != 0) {
                sb.append(", ");
            }

            sb
                    .append("new SmartConfigValue(")
                    .append(propertyName)
                    .append(".getName(), ")
                    .append(propertyName)
                    .append(".getValue())");
        }

        copyMethod.newReturn()
                .setExpression(vm.newVar("new SmartConfigImpl(" + sb.toString() + ")"));
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

        List<String> propertiesNames = allProperties
                .getAllProperties()
                .keySet()
                .stream()
                .sorted()
                .collect(toList());

        for (String propertyName : propertiesNames) {
            expression.append(propertyName);
            expression.append(", ");
        }

        expression.deleteCharAt(expression.length() - 1);
        expression.deleteCharAt(expression.length() - 1);

        expression.append(")");

        allPropertiesField.setExpression(vm.newVar(expression.toString()));
    }
}
