package com.bdev.smart.config.generator.properties;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.property.Property;
import com.bdev.smart.config.generator.utils.SmartConfigImports;
import com.bdev.smart.config.generator.utils.SmartConfigNames;
import com.bdev.smart.config.generator.utils.SmartConfigNamespace;
import com.bdev.smart.config.generator.utils.SmartConfigTypesMatcher;
import net.sourceforge.jenesis4java.AbstractMethod;
import net.sourceforge.jenesis4java.CompilationUnit;
import net.sourceforge.jenesis4java.Interface;
import net.sourceforge.jenesis4java.VirtualMachine;

import static net.sourceforge.jenesis4java.Access.PUBLIC;

public class SmartConfigGenerator {
    public static void generate(VirtualMachine vm, String rootPath, ConfigInfo configInfo)
            throws Exception {

        CompilationUnit unit = vm.newCompilationUnit(rootPath);

        unit.setNamespace(SmartConfigNamespace.VALUE);

        unit.addImport(SmartConfigImports.LIST_IMPORT);
        unit.addImport(SmartConfigImports.SMART_CONFIG_VALUE_IMPORT);
        unit.addImport(SmartConfigImports.OPTIONAL_IMPORT);

        Interface smartConfigPropertiesInterface = unit.newInterface("SmartConfig");

        smartConfigPropertiesInterface.setAccess(PUBLIC);

        for (String propertyName : configInfo.getAllProperties().getAllProperties().keySet()) {
            Property property = configInfo.getAllProperties().getAllProperties().get(propertyName);

            smartConfigPropertiesInterface.newMethod(
                    vm.newType(SmartConfigTypesMatcher.getConfigType(property.getType())),
                    SmartConfigNames.getPropertyConfigAccessorName(propertyName)
            );

            smartConfigPropertiesInterface.newMethod(
                    vm.newType(SmartConfigTypesMatcher.getType(property.getType(), true)),
                    SmartConfigNames.getPropertyAccessorName(propertyName)
            );
        }

        generateFindPropertyByNameMethod(vm, smartConfigPropertiesInterface);

        unit.encode();
    }

    private static void generateFindPropertyByNameMethod(
            VirtualMachine vm,
            Interface smartConfigPropertiesInterface
    ) {
        AbstractMethod findPropertyByNameMethod = smartConfigPropertiesInterface
                .newMethod(
                        vm.newType("<T> Optional<SmartConfigValue<T>>"),
                        "findPropertyByName"
                );

        findPropertyByNameMethod.addParameter(vm.newType("String"), "propertyName");
    }
}
