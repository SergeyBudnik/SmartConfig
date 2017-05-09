package com.bdev.smart.config.generator;

import com.bdev.smart.config.data.inner.ConfigInfo;
import com.bdev.smart.config.data.inner.PropertyInfo;
import com.bdev.smart.config.data.inner.PropertyType;
import net.sourceforge.jenesis4java.*;

class SmartConfigPropertiesInterfaceGenerator {
    static void generate(
            VirtualMachine vm,
            String rootPath,
            ConfigInfo configInfo
    ) throws Exception {
        CompilationUnit unit = vm.newCompilationUnit(rootPath);

        unit.setNamespace("com.bdev.smart.config");

        unit.addImport("java.util.List");
        unit.addImport("com.bdev.smart.config.data.SmartConfigValue");

        Interface smartConfigPropertiesInterface = unit.newInterface("SmartConfig");

        smartConfigPropertiesInterface.setAccess(Access.PUBLIC);

        for (String propertyName : configInfo.getPropertiesInfo().keySet()) {
            PropertyInfo propertyInfo = configInfo.getPropertiesInfo().get(propertyName);

            smartConfigPropertiesInterface.newMethod(
                    vm.newType(getType(propertyInfo.getType())),
                    getMethodName(propertyName)
            );
        }

        unit.encode();
    }

    private static String getMethodName(String propertyName) {
        return "get" +
                propertyName.substring(0, 1).toUpperCase() +
                propertyName.substring(1);
    }

    private static String getType(PropertyType propertyType) {
        switch (propertyType) {
            case NUMBER:
                return "SmartConfigValue<Long>";
            case BOOLEAN:
                return "SmartConfigValue<Boolean>";
            case STRING:
                return "SmartConfigValue<String>";
            case LIST_OF_NUMBERS:
                return "SmartConfigValue<List<Long>>";
            case LIST_OF_BOOLEANS:
                return "SmartConfigValue<List<Boolean>>";
            case LIST_OF_STRINGS:
                return "SmartConfigValue<List<String>>";
        }

        throw new RuntimeException();
    }
}
