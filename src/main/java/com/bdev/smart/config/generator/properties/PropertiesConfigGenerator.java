package com.bdev.smart.config.generator.properties;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.property.PropertyInfo;
import com.bdev.smart.config.generator.utils.SmartConfigImports;
import com.bdev.smart.config.generator.utils.SmartConfigNamesMatcher;
import com.bdev.smart.config.generator.utils.SmartConfigNamespace;
import com.bdev.smart.config.generator.utils.SmartConfigTypesMatcher;
import net.sourceforge.jenesis4java.Access;
import net.sourceforge.jenesis4java.CompilationUnit;
import net.sourceforge.jenesis4java.Interface;
import net.sourceforge.jenesis4java.VirtualMachine;

public class PropertiesConfigGenerator {
    public static void generate(
            VirtualMachine vm,
            String rootPath,
            ConfigInfo configInfo
    ) throws Exception {
        CompilationUnit unit = vm.newCompilationUnit(rootPath);

        unit.setNamespace(SmartConfigNamespace.VALUE);

        unit.addImport(SmartConfigImports.LIST_IMPORT);
        unit.addImport(SmartConfigImports.SMART_CONFIG_VALUE_IMPORT);

        Interface smartConfigPropertiesInterface = unit.newInterface("SmartConfig");

        smartConfigPropertiesInterface.setAccess(Access.PUBLIC);

        for (String propertyName : configInfo.getPropertiesInfo().keySet()) {
            PropertyInfo propertyInfo = configInfo.getPropertiesInfo().get(propertyName);

            smartConfigPropertiesInterface.newMethod(
                    vm.newType(SmartConfigTypesMatcher.getType(propertyInfo.getType())),
                    SmartConfigNamesMatcher.getPropertyAccessorName(propertyName)
            );
        }

        unit.encode();
    }
}
